package com.tolunaykandirmaz.signverify.rest.client;

import com.tolunaykandirmaz.signverify.rest.model.RestClientResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RestClient {

    @Multipart
    @POST("api/v1/verify")
    Call<RestClientResponse> verifySignature(@Part MultipartBody.Part queryImage, @Part MultipartBody.Part referenceImage);
}
