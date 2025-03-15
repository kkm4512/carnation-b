package com.example.carnation.common.exception;

import com.example.carnation.common.response.enums.ApiResponseEnum;

public class ValidatorException extends BaseException {
    public ValidatorException(ApiResponseEnum apiResponseEnum) {
        super(apiResponseEnum);
    }

    public ValidatorException(ApiResponseEnum apiResponseEnum, Throwable cause) {
        super(apiResponseEnum, cause);
    }
}
