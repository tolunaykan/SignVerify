package com.tolunaykandirmaz.signverify;

import android.animation.Animator;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.tolunaykandirmaz.signverify.rest.model.BaseResponse;
import com.tolunaykandirmaz.signverify.rest.model.DetectResponse;
import com.tolunaykandirmaz.signverify.rest.model.ResponseListener;
import com.tolunaykandirmaz.signverify.rest.model.VerifyResponse;
import com.tolunaykandirmaz.signverify.rest.model.VerifyResponseListener;
import com.tolunaykandirmaz.signverify.service.RestClientService;

import java.util.List;

public class CropActivity extends AppCompatActivity {

    private ImageView imageView;

    private CropImageView cropImageView;

    private LottieAnimationView lottieAnimationView;

    boolean isReferenceSignatureDocument;

    private ConstraintLayout resultLayout;

    private LottieAnimationView resultLottieView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);

        final Bitmap signatureDocument = Utils.passBitmapHelper;
        Utils.referenceImageSelected = false;
        cropImageView = findViewById(R.id.signature_image_crop);
        imageView = findViewById(R.id.signature_image);
        lottieAnimationView = findViewById(R.id.animationView2);
        Button verifyButton = findViewById(R.id.verifyButton);
        resultLayout = findViewById(R.id.result_layout);
        resultLottieView = resultLayout.findViewById(R.id.animationView3);

        isReferenceSignatureDocument = getIntent().getBooleanExtra("isReferenceSignatureDocument", false);

        imageView.setImageBitmap(signatureDocument);
        cropImageView.setImageBitmap(signatureDocument);

        resultLottieView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                finish();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        verifyButton.setOnClickListener(v -> {

            if (isReferenceSignatureDocument) {
                Utils.passBitmapHelper = cropImageView.getCroppedImage();
                Utils.referenceImageSelected = true;
                finish();
            } else {

                final Bitmap croppedSignature = cropImageView.getCroppedImage();

                imageView.setImageBitmap(croppedSignature);
                cropImageView.setVisibility(View.GONE);
                verifyButton.setVisibility(View.GONE);
                showAnimation();
                try {
                    verifySignature(croppedSignature);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), getString(R.string.signaure_verify_failure), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

        });

        try {
            detectSignature(signatureDocument);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Bir hata oluştu", Toast.LENGTH_SHORT).show();
        }
    }

    private void verifySignature(Bitmap queryImage) throws Exception {

        final Bitmap referenceBitmap = Utils.getImage(getApplicationContext(), Constants.REFERENCE_IMAGE_NAME);

        final RestClientService restClientService = RestClientService.getInstance(getApplicationContext());
        restClientService.getResult(queryImage, referenceBitmap, new ResponseListener() {
            @Override
            public void onSuccess(BaseResponse baseResponse) {
                final VerifyResponse verifyResponse = (VerifyResponse) baseResponse;

                closeAnimation();
                showResultLayout(verifyResponse.getIsReal());
            }

            @Override
            public void onFailure() {
                finish();
                Toast.makeText(getApplicationContext(), getString(R.string.signaure_verify_failure), Toast.LENGTH_SHORT).show();
            }
        });
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

    private void showResultLayout(Integer isReal) {

        if (isReal == 1) {
            resultLottieView.setAnimation(R.raw.lottie_success);
        } else {
            resultLottieView.setAnimation(R.raw.lottie_failure);
        }

        resultLayout.setVisibility(View.VISIBLE);
    }

    private void closeResultLayout() {
        resultLayout.setVisibility(View.GONE);
    }
}