package com.tolunaykandirmaz.signverify;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    private LinearLayout scanLayout;

    private ImageView imageView;

    private static final int IMAGE_CAPTURE_REQUEST = 301;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        scanLayout = findViewById(R.id.scan_layout);
        imageView = findViewById(R.id.result);

        imageView.setOnClickListener(v -> imageView.setVisibility(View.GONE));

        scanLayout.setOnClickListener(v -> takePicture());
    }

    private void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, IMAGE_CAPTURE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == IMAGE_CAPTURE_REQUEST && resultCode == RESULT_OK && data != null) {
            try {
                Bundle extras = data.getExtras();
                Bitmap bitmap = (Bitmap) extras.get("data");
                imageView.setImageBitmap(bitmap);
                imageView.setVisibility(View.VISIBLE);

            } catch (Exception e) {
                Log.d("MYTAG", "Query fotoğraf çekilemedi" + e.getLocalizedMessage());
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}