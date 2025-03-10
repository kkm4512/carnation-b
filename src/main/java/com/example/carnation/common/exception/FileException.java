package com.example.carnation.common.exception;

import com.example.carnation.common.response.enums.ApiResponseEnum;

public class FileException extends BaseException {
    public FileException(ApiResponseEnum apiResponseEnum) {
        super(apiResponseEnum);
    }

    public FileException(ApiResponseEnum apiResponseEnum, Throwable cause) {
        super(apiResponseEnum, cause);
    }
}
