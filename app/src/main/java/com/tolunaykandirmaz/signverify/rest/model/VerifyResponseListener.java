package com.tolunaykandirmaz.signverify.rest.model;

import java.util.List;

public interface VerifyResponseListener {
    void onSuccess(List<DetectResponse> detectResponseList);

    void onFailure();
}
