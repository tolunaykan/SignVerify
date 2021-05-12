package com.tolunaykandirmaz.signverify.rest.model;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;

@Getter
public class DetectResponse extends BaseResponse {

    @SerializedName("xmin")
    private Double xMin;

    @SerializedName("ymin")
    private Double yMin;

    @SerializedName("xmax")
    private Double xMax;

    @SerializedName("ymax")
    private Double yMax;

    @SerializedName("confidence")
    private Double confidence;

    public DetectResponse(String status, String message, Double xMin, Double yMin, Double xMax, Double yMax, Double confidence) {
        super(status, message);
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
        this.confidence = confidence;
    }
}
