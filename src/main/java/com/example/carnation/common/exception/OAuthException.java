package com.example.carnation.common.exception;

import com.example.carnation.common.response.enums.ApiResponseEnum;

public class OAuthException extends BaseException {
    public OAuthException(ApiResponseEnum apiResponseEnum) {
        super(apiResponseEnum);
    }

    public OAuthException(ApiResponseEnum apiResponseEnum, Throwable cause) {
        super(apiResponseEnum, cause);
    }
}
