package com.example.carnation.common.response.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CareApiResponseEnum implements ApiResponseEnum {

    // 400


    // 401

    // 403

    // 404
    CAREGIVER_NOT_FOUND(HttpStatus.NOT_FOUND, "간병인를 찾을 수 없습니다"),
    PATIENT_NOT_FOUND(HttpStatus.NOT_FOUND, "환자를 찾을 수 없습니다"),

    // 409
    ;

    private final HttpStatus httpStatus;
    private final String message;

    CareApiResponseEnum(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

}
