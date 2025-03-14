package com.example.carnation.common.response.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum SmsApiResponse implements ApiResponseEnum {

    // 400 Bad Request (잘못된 요청)
    INVALID_PHONE_NUMBER(HttpStatus.BAD_REQUEST, "유효하지 않은 전화번호 형식입니다."),

    // 403 Forbidden (권한 없음)
    UNAUTHORIZED_ACCESS(HttpStatus.FORBIDDEN, "SMS 전송 권한이 없습니다."),

    // 404 Not Found (파일 없음)
    EMPTY_RESPONSE(HttpStatus.NOT_FOUND, "SMS 서버에서 응답을 받지 못했습니다."),

    // 409 Conflict (파일 중복)
    SMS_SEND_FAILED(HttpStatus.CONFLICT, "SMS 전송에 실패했습니다."),

    // 500 Internal Server Error (서버 내부 오류)
    UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SMS 전송 중 알 수 없는 오류가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    SmsApiResponse(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
