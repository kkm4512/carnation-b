package com.example.carnation.common.response.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum AwsApiResponseEnum implements ApiResponseEnum {

    // 400

    // 401


    // 403

    // 404


    // 409

    // 500
    AWS_STORAGE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "파일 저장 중 오류가 발생했습니다."),

    // 503
    AWS_CONNECTION_ERROR(HttpStatus.SERVICE_UNAVAILABLE, "AWS 서비스에 연결할 수 없습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;

    AwsApiResponseEnum(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

}
