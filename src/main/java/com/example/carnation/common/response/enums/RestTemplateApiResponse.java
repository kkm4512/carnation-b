package com.example.carnation.common.response.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum RestTemplateApiResponse implements ApiResponseEnum {

    // 400 Bad Request

    // 408 Request Timeout
    REQUEST_TIMEOUT(HttpStatus.REQUEST_TIMEOUT, "요청 시간이 초과되었습니다"),

    // 409 Conflict
    CONFLICT(HttpStatus.CONFLICT, "요청 충돌이 발생했습니다"),

    // 502 Bad Gateway
    BAD_GATEWAY(HttpStatus.BAD_GATEWAY, "서버 게이트웨이 오류가 발생했습니다"),

    // 503 Service Unavailable
    SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "서버를 사용할 수 없습니다"),

    // 504 Gateway Timeout
    GATEWAY_TIMEOUT(HttpStatus.GATEWAY_TIMEOUT, "서버 응답 시간이 초과되었습니다"),

    // 525 SSL Handshake Failed
    SSL_HANDSHAKE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "SSL 핸드셰이크 실패")
    ;


    private final HttpStatus httpStatus;
    private final String message;

    RestTemplateApiResponse(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
