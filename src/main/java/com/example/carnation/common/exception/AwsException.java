package com.example.carnation.common.exception;

import com.example.carnation.common.response.enums.ApiResponseEnum;

public class AwsException extends BaseException {
    public AwsException(ApiResponseEnum apiResponseEnum) {
        super(apiResponseEnum);
    }

    public AwsException(ApiResponseEnum apiResponseEnum, Throwable cause) {
        super(apiResponseEnum, cause);
    }
}
