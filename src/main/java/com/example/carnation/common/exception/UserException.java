package com.example.carnation.common.exception;

import com.example.carnation.common.response.enums.ApiResponseEnum;

public class UserException extends BaseException {
    public UserException(ApiResponseEnum apiResponseEnum) {
        super(apiResponseEnum);
    }

    public UserException(ApiResponseEnum apiResponseEnum, Throwable cause) {
        super(apiResponseEnum, cause);
    }
}
