package com.tolunaykandirmaz.signverify.rest;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClientFactory {
    public static <S> S createClient(Class<S> serviceClass) {
        Interceptor interceptor = chain -> {
            final Request request = chain.request().newBuilder()
                    .addHeader("apiKey", ApiConfig.API_KEY)
                    .build();

            return chain.proceed(request);
        };

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(interceptor);
        OkHttpClient okHttpClient = httpClient.build();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiConfig.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(serviceClass);

    }
}
