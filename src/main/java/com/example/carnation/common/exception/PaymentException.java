package com.example.carnation.common.exception;

import com.example.carnation.common.response.enums.ApiResponseEnum;

public class PaymentException extends BaseException {
    public PaymentException(ApiResponseEnum apiResponseEnum) {
        super(apiResponseEnum);
    }

    public PaymentException(ApiResponseEnum apiResponseEnum, Throwable cause) {
        super(apiResponseEnum, cause);
    }
}
