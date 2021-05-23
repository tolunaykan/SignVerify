package com.tolunaykandirmaz.signverify.rest.model;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyResponse extends BaseResponse {

    private Integer isReal;

    @SerializedName("pred")
    private Double prediction;

    public VerifyResponse(String status, String message, Integer isReal, Double prediction) {
        super(status, message);
        this.isReal = isReal;
        this.prediction = prediction;
    }

}
