package com.example.carnation.common.exception;

import com.example.carnation.common.response.ApiResponse;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j(topic = "BaseDataException")
public class BaseDataException extends RuntimeException {
    private final ApiResponse<?> apiResponse;
    private final String originalMessage; // 원본 메시지 저장

    public <T> BaseDataException(ApiResponse<T> apiResponse, Throwable cause) {
        super(apiResponse.getMessage(), cause);
        this.apiResponse = apiResponse;
        this.originalMessage = apiResponse.getMessage();
    }

}
