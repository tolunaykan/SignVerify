package com.tolunaykandirmaz.signverify;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.tolunaykandirmaz.signverify.rest.model.DetectResponse;
import com.tolunaykandirmaz.signverify.rest.model.VerifyResponseListener;
import com.tolunaykandirmaz.signverify.service.RestClientService;

import java.util.List;

public class CropActivity extends AppCompatActivity {

    private ImageView imageView;

    private CropImageView cropImageView;

    private LottieAnimationView lottieAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);

        final Bitmap signatureDocument = Utils.passBitmapHelper;
        cropImageView = findViewById(R.id.signature_image_crop);
        imageView = findViewById(R.id.signature_image);
        lottieAnimationView = findViewById(R.id.animationView2);
        Button verifyButton = findViewById(R.id.verifyButton);

        imageView.setImageBitmap(signatureDocument);
        cropImageView.setImageBitmap(signatureDocument);

        verifyButton.setOnClickListener(v -> {
            imageView.setImageBitmap(cropImageView.getCroppedImage());
            cropImageView.setVisibility(View.GONE);
        });

        try {
            detectSignature(signatureDocument);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Bir hata oluştu", Toast.LENGTH_SHORT).show();
        }
    }

    private void detectSignature(Bitmap bitmap) throws Exception {
        showAnimation();

        final RestClientService restClientService = RestClientService.getInstance(getApplicationContext());
        restClientService.detectSignature(bitmap, new VerifyResponseListener() {
            @Override
            public void onSuccess(List<DetectResponse> detectResponseList) {
                closeAnimation();
                showCropLayout(detectResponseList);
            }

            @Override
            public void onFailure() {
                closeAnimation();
                Toast.makeText(getApplicationContext(), "Bir hata oluştu: onFailure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showCropLayout(List<DetectResponse> detectResponseList) {

        if (detectResponseList != null && !detectResponseList.isEmpty()) {
            final DetectResponse detectResponse = detectResponseList.get(0);

            final Rect rectangle = new Rect(
                    detectResponse.getXMin().intValue(),
                    detectResponse.getYMin().intValue(),
                    detectResponse.getXMax().intValue(),
                    detectResponse.getYMax().intValue()
            );

            cropImageView.setCropRect(rectangle);
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.signature_not_detected), Toast.LENGTH_SHORT).show();
        }

        cropImageView.setVisibility(View.VISIBLE);
    }

    private void showAnimation() {
        lottieAnimationView.setVisibility(View.VISIBLE);
    }

    private void closeAnimation() {
        lottieAnimationView.setVisibility(View.GONE);
    }
}