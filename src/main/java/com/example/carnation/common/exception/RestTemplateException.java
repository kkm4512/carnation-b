package com.example.carnation.common.exception;

import com.example.carnation.common.response.enums.ApiResponseEnum;

public class RestTemplateException extends BaseException {
    public RestTemplateException(ApiResponseEnum apiResponseEnum) {
        super(apiResponseEnum);
    }

    public RestTemplateException(ApiResponseEnum apiResponseEnum, Throwable cause) {
        super(apiResponseEnum, cause);
    }
}
