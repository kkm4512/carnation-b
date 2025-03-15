package com.example.carnation.common.response.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ValidatorApiResponseEnum implements ApiResponseEnum {

    // 400 Bad Request (잘못된 요청)
    NULL_VALUE_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "비교할 값 중 하나가 null입니다.")
    ;

    // 403 Forbidden (권한 없음)

    // 404 Not Found (파일 없음)

    // 409 Conflict (파일 중복)

    // 500 Internal Server Error (서버 내부 오류)

    private final HttpStatus httpStatus;
    private final String message;

    ValidatorApiResponseEnum(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
