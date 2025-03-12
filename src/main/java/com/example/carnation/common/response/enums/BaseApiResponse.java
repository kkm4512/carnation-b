package com.example.carnation.common.response.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum BaseApiResponse implements ApiResponseEnum {

    //200
    SUCCESS(HttpStatus.OK, "요청하신 작업에 성공 하였습니다"),

    // 400
    FAIL(HttpStatus.BAD_REQUEST,"요청하신 작업에 실패 하였습니다"),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다"),

    // 404
    NOT_FOUND(HttpStatus.NOT_FOUND, "데이터를 찾을 수 없습니다"),

    // 405
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "지원하지 않는 HTTP 요청 입니다"),

    // 409
    CONFLICT(HttpStatus.CONFLICT,"중복값이 발생 하였습니다"),

    // 500
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류가 발생했습니다, 관리자에게 문의 해주세요");
    ;

    private final HttpStatus httpStatus;
    private final String message;

    BaseApiResponse(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

}
