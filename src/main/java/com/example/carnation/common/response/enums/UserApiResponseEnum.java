package com.example.carnation.common.response.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum UserApiResponseEnum implements ApiResponseEnum {

    // 400
    INVALID_CREDENTIALS(HttpStatus.BAD_REQUEST, "아이디 또는 비밀번호가 일치하지 않습니다"),
    NULL_USER(HttpStatus.BAD_REQUEST, "유저의 값이 존재하지 않습니다"),
    LOG_OUT_FAIL(HttpStatus.BAD_REQUEST,"로그아웃 시 유저의 ID 정보가 필요합니다."),

    // 401
    JWT_INVALID(HttpStatus.UNAUTHORIZED, "유효하지 않은 JWT입니다"),
    JWT_EXPIRED(HttpStatus.UNAUTHORIZED, "만료된 JWT 토큰입니다"),
    JWT_UNSUPPORTED(HttpStatus.UNAUTHORIZED, "지원되지 않는 JWT입니다"),
    JWT_NOT_SIGNATURED(HttpStatus.UNAUTHORIZED, "서명되지 않은 JWT입니다"),
    NOT_LOGIN(HttpStatus.UNAUTHORIZED,"로그인후 이용해 주세요"),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 Refresh Token입니다."),

    // 403
    NOT_ME(HttpStatus.FORBIDDEN,"권한이 존재 하지 않습니다"),

    // 404
    JWT_NOT_FOUND(HttpStatus.NOT_FOUND, "토큰을 찾을 수 없습니다"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다"),

    // 409
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이메일이 중복 되었습니다"),
    EXISTING_SOCIAL_ACCOUNT(HttpStatus.CONFLICT, "이미 소셜 계정으로 가입된 사용자입니다. 소셜 로그인을 이용해 주세요.")
    ;

    private final HttpStatus httpStatus;
    private final String message;

    UserApiResponseEnum(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

}
