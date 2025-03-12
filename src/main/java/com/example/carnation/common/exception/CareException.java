package com.example.carnation.common.exception;

import com.example.carnation.common.response.enums.ApiResponseEnum;

public class CareException extends BaseException {
    public CareException(ApiResponseEnum apiResponseEnum) {
        super(apiResponseEnum);
    }

    public CareException(ApiResponseEnum apiResponseEnum, Throwable cause) {
        super(apiResponseEnum, cause);
    }
}
