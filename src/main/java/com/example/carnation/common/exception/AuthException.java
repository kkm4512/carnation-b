package com.example.carnation.common.exception;

import com.example.carnation.common.response.enums.ApiResponseEnum;

public class AuthException extends BaseException {
    public AuthException(ApiResponseEnum apiResponseEnum) {
        super(apiResponseEnum);
    }

    public AuthException(ApiResponseEnum apiResponseEnum, Throwable cause) {
        super(apiResponseEnum, cause);
    }
}
