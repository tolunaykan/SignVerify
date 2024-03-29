package com.tolunaykandirmaz.signverify;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    private LinearLayout scanLayout;

    private String currentPhotoPath;

    private static final int IMAGE_CAPTURE_REQUEST = 301;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scanLayout = findViewById(R.id.scan_layout);

        scanLayout.setOnClickListener(v -> takePicture());

    }

    private void takePicture() {
        try {
            dispatchTakePictureIntent();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Fotoğraf çekilirken bir hata oluştu", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == IMAGE_CAPTURE_REQUEST && resultCode == RESULT_OK) {

            File file = new File(currentPhotoPath);
            String filePath = file.getPath();
            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
            try {
                bitmap = rotateBitmap(filePath, bitmap);
                startCropActivity(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Bir hata oluştu: Failure in rotating", Toast.LENGTH_SHORT).show();
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void startCropActivity(Bitmap image) {
        final Intent cropActivity = new Intent(MainActivity.this, CropActivity.class);
        //Utils.passBitmapHelper = BitmapFactory.decodeResource(getResources(), R.drawable.uji44a00); // TODO
        Utils.passBitmapHelper = image;

        startActivity(cropActivity);
    }

    private File createImageFile() throws IOException {
        String imageFileName = "signatureDocument";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() throws IOException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = createImageFile();

            Uri photoURI = FileProvider.getUriForFile(this, getApplicationContext().getPackageName(), photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, IMAGE_CAPTURE_REQUEST);

        }
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
}