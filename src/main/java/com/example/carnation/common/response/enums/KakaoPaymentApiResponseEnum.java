package com.example.carnation.common.response.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum KakaoPaymentApiResponseEnum implements ApiResponseEnum {

    // 400 Bad Request (잘못된 요청)


    // 403 Forbidden


    // 404 Not Found
    KAKAO_PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "카카오 결제를 찾을 수 없습니다."),

    // 409 Conflict

    // 410 Gone (만료)

    // 500 Internal Server Error (서버 내부 오류)
    ;

    private final HttpStatus httpStatus;
    private final String message;

    KakaoPaymentApiResponseEnum(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
