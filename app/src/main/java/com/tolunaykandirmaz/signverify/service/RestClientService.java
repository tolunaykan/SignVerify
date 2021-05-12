package com.tolunaykandirmaz.signverify.service;

import android.content.Context;
import android.graphics.Bitmap;

import com.tolunaykandirmaz.signverify.Utils;
import com.tolunaykandirmaz.signverify.rest.RestClientFactory;
import com.tolunaykandirmaz.signverify.rest.client.RestClient;
import com.tolunaykandirmaz.signverify.rest.model.DetectResponse;
import com.tolunaykandirmaz.signverify.rest.model.ResponseListener;
import com.tolunaykandirmaz.signverify.rest.model.VerifyResponse;
import com.tolunaykandirmaz.signverify.rest.model.VerifyResponseListener;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RestClientService {

    private static final String QUERY_IMAGE_REQUEST_KEY = "queryImage";

    private static final String REFERENCE_IMAGE_REQUEST_KEY = "referenceImage";

    private static final String SIGNATURE_DOCUMENT_IMAGE_REQUEST_KEY = "image";

    private static final String QUERY_IMAGE_NAME = "queryImage.png";

    private static final String REFERENCE_IMAGE_NAME = "queryImage.png";

    private static final String SIGNATURE_DOCUMENT_IMAGE_NAME = "signatureDocumentImage.png";

    private final Context context;

    private final RestClient restClient;

    public static RestClientService restClientService;

    private RestClientService(Context context) {
        restClient = RestClientFactory.createClient(RestClient.class);
        this.context = context;
    }

    public static RestClientService getInstance(Context context) {
        if (restClientService == null) {
            restClientService = new RestClientService(context);
        }

        return restClientService;
    }

    public void getResult(Bitmap queryBitmap, Bitmap referenceBitmap, ResponseListener listener) throws Exception {

        final MultipartBody.Part queryImagePart = createImagePart(queryBitmap, QUERY_IMAGE_NAME, QUERY_IMAGE_REQUEST_KEY);
        final MultipartBody.Part referenceImagePart = createImagePart(referenceBitmap, REFERENCE_IMAGE_NAME, REFERENCE_IMAGE_REQUEST_KEY);

        restClient.verifySignature(queryImagePart, referenceImagePart).enqueue(new Callback<VerifyResponse>() {
            @Override
            public void onResponse(Call<VerifyResponse> call, Response<VerifyResponse> response) {
                final VerifyResponse verifyResponse = response.body();
                listener.onSuccess(verifyResponse);
            }

            @Override
            public void onFailure(Call<VerifyResponse> call, Throwable t) {
                listener.onFailure();
            }
        });
    }

    public void detectSignature(Bitmap image, VerifyResponseListener listener) throws Exception {

        final MultipartBody.Part imagePart = createImagePart(image, SIGNATURE_DOCUMENT_IMAGE_NAME, SIGNATURE_DOCUMENT_IMAGE_REQUEST_KEY);

        restClient.detectSignature(imagePart).enqueue(new Callback<List<DetectResponse>>() {
            @Override
            public void onResponse(Call<List<DetectResponse>> call, Response<List<DetectResponse>> response) {
                final List<DetectResponse> detectResponse = response.body();
                listener.onSuccess(detectResponse);
            }

            @Override
            public void onFailure(Call<List<DetectResponse>> call, Throwable t) {
                listener.onFailure();
            }
        });
    }

    private MultipartBody.Part createImagePart(Bitmap bitmap, String fileName, String requestKey) throws Exception {
        File file = Utils.convertBitmapToFile(context, bitmap, fileName);
        if (file == null) {
            throw new Exception("bitmap file'a çevrilemedi");
        }

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        return MultipartBody.Part.createFormData(requestKey, file.getName(), requestBody);

    }
}
