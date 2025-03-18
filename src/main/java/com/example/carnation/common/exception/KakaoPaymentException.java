package com.example.carnation.common.exception;

import com.example.carnation.common.response.enums.ApiResponseEnum;

public class KakaoPaymentException extends BaseException {
    public KakaoPaymentException(ApiResponseEnum apiResponseEnum) {
        super(apiResponseEnum);
    }

    public KakaoPaymentException(ApiResponseEnum apiResponseEnum, Throwable cause) {
        super(apiResponseEnum, cause);
    }
}
