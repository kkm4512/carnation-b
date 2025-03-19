package com.example.carnation.common.exception;

import com.example.carnation.common.response.enums.ApiResponseEnum;

public class ProductException extends BaseException {
    public ProductException(ApiResponseEnum apiResponseEnum) {
        super(apiResponseEnum);
    }

    public ProductException(ApiResponseEnum apiResponseEnum, Throwable cause) {
        super(apiResponseEnum, cause);
    }
}
