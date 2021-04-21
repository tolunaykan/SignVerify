package com.tolunaykandirmaz.signverify;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.tolunaykandirmaz.signverify.rest.model.BaseResponse;
import com.tolunaykandirmaz.signverify.rest.model.ResponseListener;
import com.tolunaykandirmaz.signverify.service.RestClientService;


public class MainActivity extends AppCompatActivity {

    private LinearLayout scanLayout;

    private ConstraintLayout loadingLayout;

    private ConstraintLayout resultLayout;

    private LottieAnimationView resultLottieView;

    private ImageView loadingImageView;

    private static final int IMAGE_CAPTURE_REQUEST = 301;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        scanLayout = findViewById(R.id.scan_layout);
        loadingLayout = findViewById(R.id.loading_layout);
        resultLayout = findViewById(R.id.result_layout);
        loadingImageView = loadingLayout.findViewById(R.id.signature_image);
        resultLottieView = resultLayout.findViewById(R.id.animationView3);

        scanLayout.setOnClickListener(v -> takePicture());

        resultLottieView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                closeResultLayout();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
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
                verifySignature(bitmap);

            } catch (Exception e) {
                Log.d("MYTAG", "İmza onaylama işlemi başarısız" + e.getLocalizedMessage());
                Toast.makeText(getApplicationContext(), getString(R.string.signaure_verify_failure), Toast.LENGTH_SHORT).show();
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void verifySignature(Bitmap queryImage) throws Exception {

        loadingImageView.setImageBitmap(queryImage);
        showLoadingLayout();
        final Bitmap referenceBitmap = Utils.getImage(getApplicationContext(), Constants.REFERENCE_IMAGE_NAME);

        final RestClientService restClientService = RestClientService.getInstance(getApplicationContext());
        restClientService.getResult(queryImage, referenceBitmap, new ResponseListener() {
            @Override
            public void onSuccess(BaseResponse baseResponse) {
                closeLoadingLayout();
                showResultLayout();
            }

            @Override
            public void onFailure() {
                closeLoadingLayout();
            }
        });
        //Toast.makeText(getApplicationContext(), String.valueOf(response.getIsReal()), Toast.LENGTH_SHORT).show();
    }

    private void showLoadingLayout() {
        loadingLayout.setVisibility(View.VISIBLE);
    }

    private void closeLoadingLayout() {
        loadingLayout.setVisibility(View.GONE);
    }

    private void showResultLayout() {
        resultLayout.setVisibility(View.VISIBLE);
    }

    private void closeResultLayout() {
        resultLayout.setVisibility(View.GONE);
    }
}