package com.tolunaykandirmaz.signverify.rest.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyResponse extends BaseResponse {

    private Integer isReal;

    public VerifyResponse(String status, String message, Integer isReal) {
        super(status, message);
        this.isReal = isReal;
    }

}
