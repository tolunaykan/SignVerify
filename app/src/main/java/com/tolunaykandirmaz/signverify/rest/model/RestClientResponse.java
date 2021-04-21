package com.tolunaykandirmaz.signverify.rest.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestClientResponse extends BaseResponse {

    private Integer isReal;

    public RestClientResponse(String status, String message, Integer isReal) {
        super(status, message);
        this.isReal = isReal;
    }

}
