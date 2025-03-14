package com.example.carnation.common.exception;

import com.example.carnation.common.response.enums.ApiResponseEnum;

public class SmsException extends BaseException {
    public SmsException(ApiResponseEnum apiResponseEnum) {
        super(apiResponseEnum);
    }

    public SmsException(ApiResponseEnum apiResponseEnum, Throwable cause) {
        super(apiResponseEnum, cause);
    }
}
