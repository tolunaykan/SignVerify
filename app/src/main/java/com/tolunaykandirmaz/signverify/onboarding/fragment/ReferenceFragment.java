package com.tolunaykandirmaz.signverify.onboarding.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.github.appintro.SlidePolicy;
import com.tolunaykandirmaz.signverify.Constants;
import com.tolunaykandirmaz.signverify.CropActivity;
import com.tolunaykandirmaz.signverify.R;
import com.tolunaykandirmaz.signverify.Utils;

import java.io.File;
import java.io.IOException;

import lombok.RequiredArgsConstructor;

import static android.app.Activity.RESULT_OK;

@RequiredArgsConstructor
public class ReferenceFragment extends Fragment implements SlidePolicy {

    private ImageView imageView;

    private final String title;

    private final String description;

    private final int image;

    private boolean isPictureChosen = false;

    private static final int IMAGE_PICK_REQUEST = 300;

    private static final int IMAGE_CAPTURE_REQUEST = 301;

    private String currentPhotoPath;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.onboarding_reference_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView titleTextView = view.findViewById(R.id.title);
        TextView descriptionTextView = view.findViewById(R.id.description);
        imageView = view.findViewById(R.id.image);
        final Button takePictureButton = view.findViewById(R.id.take_picture_button);
        final Button selectPictureButton = view.findViewById(R.id.select_picture_button);

        titleTextView.setText(title);
        descriptionTextView.setText(description);
        imageView.setImageResource(image);

        takePictureButton.setOnClickListener(v -> takePicture());
        selectPictureButton.setOnClickListener(v -> pickFromGallery());

    }
    

    @Override
    public boolean isPolicyRespected() {
        return isPictureChosen;
    }

    @Override
    public void onUserIllegallyRequestedNextPage() {
        Toast.makeText(requireContext(), requireContext().getString(R.string.chose_picture_warning), Toast.LENGTH_SHORT).show();
    }

    private void pickFromGallery() {
        Intent galleryPickIntent = new Intent(Intent.ACTION_PICK);
        galleryPickIntent.setType("image/*");
        Utils.referenceImageSelected = false;
        startActivityForResult(galleryPickIntent, IMAGE_PICK_REQUEST);
    }

    private void takePicture() {
        try {
            dispatchTakePictureIntent();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Fotoğraf çekilirken bir hata oluştu", Toast.LENGTH_SHORT).show();
        }
    }

    private void dispatchTakePictureIntent() throws IOException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(requireContext().getPackageManager()) != null) {
            File photoFile = createImageFile();

            Uri photoURI = FileProvider.getUriForFile(requireContext(), requireContext().getPackageName(), photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, IMAGE_CAPTURE_REQUEST);

        }
    }

    private File createImageFile() throws IOException {
        String imageFileName = "referenceSignatureDocument";
        File storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_REQUEST && data != null) {
            Uri imageUri = data.getData();
            Bitmap bitmap = null;
            try {
                if (android.os.Build.VERSION.SDK_INT >= 29) {
                    bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(requireContext().getContentResolver(), imageUri));
                } else {
                    bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), imageUri);
                }

                startCropActivity(bitmap);
            } catch (Exception e) {
                Log.d("MYTAG", "Referans fotoğraf seçilemedi" + e.getLocalizedMessage());

            }
        } else if (requestCode == IMAGE_CAPTURE_REQUEST && resultCode == RESULT_OK) {
            File file = new File(currentPhotoPath);
            String filePath = file.getPath();
            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
            try {
                bitmap = rotateBitmap(filePath, bitmap);
                startCropActivity(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(requireContext(), "Bir hata oluştu: Failure in rotating", Toast.LENGTH_SHORT).show();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private Bitmap rotateBitmap(String path, Bitmap bitmap) throws IOException {
        ExifInterface ei = new ExifInterface(path);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

        Bitmap rotatedBitmap = null;
        switch (orientation) {

            case ExifInterface.ORIENTATION_ROTATE_90:
                rotatedBitmap = Utils.rotateImage(bitmap, 90);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                rotatedBitmap = Utils.rotateImage(bitmap, 180);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                rotatedBitmap = Utils.rotateImage(bitmap, 270);
                break;

            case ExifInterface.ORIENTATION_NORMAL:
            default:
                rotatedBitmap = bitmap;
        }

        return rotatedBitmap;
    }

    private void startCropActivity(Bitmap image) {
        final Intent cropActivity = new Intent(requireActivity(), CropActivity.class);
        cropActivity.putExtra("isReferenceSignatureDocument", true);
        Utils.passBitmapHelper = image;
        startActivity(cropActivity);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (Utils.referenceImageSelected) {
            //imageView.setImageBitmap(Utils.passBitmapHelper);
            Toast.makeText(requireContext(), requireContext().getString(R.string.reference_signature_selected), Toast.LENGTH_SHORT).show();
            isPictureChosen = true;

            try {
                Utils.saveImage(requireContext(), Utils.passBitmapHelper, Constants.REFERENCE_IMAGE_NAME);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(requireContext(), requireContext().getString(R.string.failure_while_saving_ref_signature), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
