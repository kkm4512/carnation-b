package com.example.carnation.common.exception;

import com.example.carnation.common.response.enums.ApiResponseEnum;

public class OrderException extends BaseException {
    public OrderException(ApiResponseEnum apiResponseEnum) {
        super(apiResponseEnum);
    }

    public OrderException(ApiResponseEnum apiResponseEnum, Throwable cause) {
        super(apiResponseEnum, cause);
    }
}
