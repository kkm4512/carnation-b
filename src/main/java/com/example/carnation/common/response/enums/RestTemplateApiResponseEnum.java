package com.example.carnation.common.response.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum RestTemplateApiResponseEnum implements ApiResponseEnum {

    // 400 Bad Request
    FAILED_TO_FETCH_SOCIAL_ACCESS_TOKEN(HttpStatus.BAD_REQUEST, "소셜 로그인 액세스 토큰을 가져오는데 실패했습니다."),
    FAILED_TO_FETCH_SOCIAL_USER_INFO(HttpStatus.BAD_REQUEST, "소셜 로그인 사용자 정보를 가져오는데 실패했습니다."),
    INVALID_SOCIAL_RESPONSE(HttpStatus.BAD_REQUEST, "소셜 로그인 응답 데이터가 유효하지 않습니다."),
    INVALID_SOCIAL_USER_INFO(HttpStatus.BAD_REQUEST, "소셜 로그인 사용자 정보가 유효하지 않습니다."),

    // 401 Unauthorized
    UNAUTHORIZED_SOCIAL_ACCESS(HttpStatus.UNAUTHORIZED, "소셜 로그인 인증이 실패했습니다. 액세스 토큰이 유효하지 않습니다."),

    // 403 Forbidden
    FORBIDDEN_SOCIAL_ACCESS(HttpStatus.FORBIDDEN, "소셜 로그인 요청이 거부되었습니다."),

    // 408 Request Timeout
    REQUEST_TIMEOUT(HttpStatus.REQUEST_TIMEOUT, "요청 시간이 초과되었습니다"),

    // 409 Conflict
    CONFLICT(HttpStatus.CONFLICT, "요청 충돌이 발생했습니다"),

    // 500 Internal Server Error
    OAUTH_PROVIDER_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "OAuth 제공자의 서버에서 오류가 발생했습니다."),
    UNEXPECTED_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "예상치 못한 서버 오류가 발생했습니다."),

    // 502 Bad Gateway
    BAD_GATEWAY(HttpStatus.BAD_GATEWAY, "서버 게이트웨이 오류가 발생했습니다"),

    // 503 Service Unavailable
    SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "서버를 사용할 수 없습니다"),
    OAUTH_PROVIDER_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "OAuth 제공자가 응답하지 않습니다. 나중에 다시 시도해주세요."),

    // 504 Gateway Timeout
    GATEWAY_TIMEOUT(HttpStatus.GATEWAY_TIMEOUT, "서버 응답 시간이 초과되었습니다"),
    OAUTH_PROVIDER_TIMEOUT(HttpStatus.GATEWAY_TIMEOUT, "OAuth 제공자 응답 시간이 초과되었습니다."),

    // 525 SSL Handshake Failed
    SSL_HANDSHAKE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "SSL 핸드셰이크 실패")
    ;

    private final HttpStatus httpStatus;
    private final String message;

    RestTemplateApiResponseEnum(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
