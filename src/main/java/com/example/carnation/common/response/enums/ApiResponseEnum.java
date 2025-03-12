package com.example.carnation.common.response.enums;

import org.springframework.http.HttpStatus;

public interface ApiResponseEnum {
    HttpStatus getHttpStatus();
    String getMessage();
}
