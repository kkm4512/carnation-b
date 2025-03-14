package com.example.carnation.common.response.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum AuthApiResponse implements ApiResponseEnum {

    // 400 Bad Request (잘못된 요청)
    INVALID_VERIFICATION_CODE(HttpStatus.BAD_REQUEST, "잘못된 인증 코드입니다."),
    MISMATCHED_VERIFICATION_CODE(HttpStatus.BAD_REQUEST, "인증 코드가 일치하지 않습니다."),


    // 403 Forbidden

    // 404 Not Found
    VERIFICATION_CODE_NOT_FOUND(HttpStatus.NOT_FOUND, "인증 코드 또는 휴대폰 번호가 존재하지 않습니다."),

    // 409 Conflict

    // 410 만료
    EXPIRED_VERIFICATION_CODE(HttpStatus.GONE, "인증 코드가 만료되었습니다. 다시 요청해주세요.")
    ;

    // 500 Internal Server Error (서버 내부 오류)

    private final HttpStatus httpStatus;
    private final String message;

    AuthApiResponse(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
