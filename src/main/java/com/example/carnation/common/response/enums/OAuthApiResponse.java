package com.example.carnation.common.response.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum OAuthApiResponse implements ApiResponseEnum {

    // 400

    // 401


    // 403

    // 404
    OAUTH_NOT_FOUND_PROVIDER(HttpStatus.NOT_FOUND, "일치하는 소셜 도매인을 찾을 수 없습니다"),

    // 409

    // 500

    // 503
    ;

    private final HttpStatus httpStatus;
    private final String message;

    OAuthApiResponse(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

}
