package com.tolunaykandirmaz.signverify.rest.client;

import com.tolunaykandirmaz.signverify.rest.model.DetectResponse;
import com.tolunaykandirmaz.signverify.rest.model.VerifyResponse;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RestClient {

    @Multipart
    @POST("api/v1/verify")
    Call<VerifyResponse> verifySignature(@Part MultipartBody.Part queryImage, @Part MultipartBody.Part referenceImage);

    @Multipart
    @POST("api/v1/detect")
    Call<List<DetectResponse>> detectSignature(@Part MultipartBody.Part image);
}
