package com.tolunaykandirmaz.signverify.onboarding.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.appintro.SlidePolicy;
import com.tolunaykandirmaz.signverify.R;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FragmentWithButtons extends Fragment implements SlidePolicy {

    private TextView titleTextView, descriptionTextView;

    private ImageView imageView;

    private Button storagePermissionButton;

    private Button cameraPermissionButton;

    private final String title;

    private final String description;

    private final int image;

    private boolean isStoragePermissionConfirmed = false;

    private boolean isCameraPermissionConfirmed = false;

    private static final int STORAGE_PERMISSION_REQUEST = 200;

    private static final int CAMERA_PERMISSION_REQUEST = 201;

    private static final int IMAGE_PICK_GALLERY_REQUEST = 300;

    String[] storagePermissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};

    String[] cameraPermissions = new String[]{Manifest.permission.CAMERA};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.onboarding_permission_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        titleTextView = view.findViewById(R.id.title);
        descriptionTextView = view.findViewById(R.id.description);
        imageView = view.findViewById(R.id.image);
        storagePermissionButton = view.findViewById(R.id.storage_permission_button);
        cameraPermissionButton = view.findViewById(R.id.camera_permission_button);

        titleTextView.setText(title);
        descriptionTextView.setText(description);
        imageView.setImageResource(image);

        storagePermissionButton.setOnClickListener(v -> requestStoragePermission());
        cameraPermissionButton.setOnClickListener(v -> requestCameraPermission());

    }

    @Override
    public boolean isPolicyRespected() {
        return isStoragePermissionConfirmed && isCameraPermissionConfirmed;
    }

    @Override
    public void onUserIllegallyRequestedNextPage() {
        if(!isCameraPermissionConfirmed){
            Toast.makeText(requireContext(), requireContext().getString(R.string.came_permission_warning), Toast.LENGTH_SHORT).show();
        }

        if(!isStoragePermissionConfirmed){
            Toast.makeText(requireContext(), requireContext().getString(R.string.storage_permission_warning), Toast.LENGTH_SHORT).show();
        }

    }

    public static FragmentWithButtons newInstance(String title, String description, int image){
        return new FragmentWithButtons(title, description, image);
    }

    private boolean checkStoragePermission(){
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestStoragePermission(){
        requestPermissions(storagePermissions, STORAGE_PERMISSION_REQUEST);
    }

    private void requestCameraPermission(){
        requestPermissions(cameraPermissions, CAMERA_PERMISSION_REQUEST);
    }

    private void pickFromGallery() {
        Intent galleryPickIntent = new Intent(Intent.ACTION_PICK);
        galleryPickIntent.setType("image/*");
        startActivityForResult(galleryPickIntent,IMAGE_PICK_GALLERY_REQUEST);
    }

/*    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == IMAGE_PICK_GALLERY_REQUEST){
                if (data != null) {
                    Uri imageUri = data.getData();
                    Bitmap bitmap = null;
                    try {
                        if (android.os.Build.VERSION.SDK_INT >= 29){
                            bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(), imageUri));
                        } else{
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                        }

                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                        String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
                        Options.getInstance().setImageBase64(encodedImage);
                        View view = viewPager2.findViewWithTag("ADDPHOTOTAG");
                        if(view != null){
                            ImageView imageView = view.findViewById(R.id.pager_item_add_photo_image);
                            imageView.setImageBitmap(bitmap);
                        }
                        Toast.makeText(CreateActivity.this, R.string.photo_selected,Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Log.d(TAG, "Foto Base64e çevrilemedi = " + e.getLocalizedMessage());
                        Toast.makeText(CreateActivity.this, R.string.photo_not_selected,Toast.LENGTH_SHORT).show();
                    }

                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }*/

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == STORAGE_PERMISSION_REQUEST && grantResults.length > 0) {
            isStoragePermissionConfirmed = grantResults[0] == PackageManager.PERMISSION_GRANTED;
        }

        if (requestCode == CAMERA_PERMISSION_REQUEST && grantResults.length > 0) {
            isCameraPermissionConfirmed = grantResults[0] == PackageManager.PERMISSION_GRANTED;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
