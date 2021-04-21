package com.tolunaykandirmaz.signverify.rest.model;

public interface ResponseListener {
    void onSuccess(BaseResponse baseResponse);

    void onFailure();
}
