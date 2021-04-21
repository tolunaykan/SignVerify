package com.tolunaykandirmaz.signverify.onboarding.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;
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
import androidx.fragment.app.Fragment;

import com.github.appintro.SlidePolicy;
import com.tolunaykandirmaz.signverify.Constants;
import com.tolunaykandirmaz.signverify.R;
import com.tolunaykandirmaz.signverify.Utils;

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
        startActivityForResult(galleryPickIntent, IMAGE_PICK_REQUEST);
    }

    private void takePicture(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, IMAGE_CAPTURE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK && requestCode == IMAGE_PICK_REQUEST && data != null){
            Uri imageUri = data.getData();
            Bitmap bitmap = null;
            try {
                if (android.os.Build.VERSION.SDK_INT >= 29){
                    bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(requireContext().getContentResolver(), imageUri));
                } else{
                    bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), imageUri);
                }

                imageView.setImageBitmap(bitmap);
                //String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
                Utils.saveImage(requireContext(), bitmap, Constants.REFERENCE_IMAGE_NAME);
                isPictureChosen = true;
                Toast.makeText(requireContext(), getString(R.string.reference_signature_selected), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Log.d("MYTAG", "Referans fotoğraf seçilemedi" + e.getLocalizedMessage());

            }
        }else if(requestCode == IMAGE_CAPTURE_REQUEST && resultCode == RESULT_OK && data != null){
            try {
                Bundle extras = data.getExtras();
                Bitmap bitmap = (Bitmap) extras.get("data");
                imageView.setImageBitmap(bitmap);
                Utils.saveImage(requireContext(), bitmap, Constants.REFERENCE_IMAGE_NAME);
                isPictureChosen = true;
                Toast.makeText(requireContext(), getString(R.string.reference_signature_selected), Toast.LENGTH_SHORT).show();
            } catch (Exception e){
                Log.d("MYTAG", "Referans fotoğraf çekilemedi" + e.getLocalizedMessage());
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
