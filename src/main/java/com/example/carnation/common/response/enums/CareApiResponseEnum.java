package com.example.carnation.common.response.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CareApiResponseEnum implements ApiResponseEnum {

    // 400


    // 401

    // 403
    // 403
    USER_NOT_AUTHORIZED_FOR_CARE_MATCHING(HttpStatus.FORBIDDEN, "해당 매칭을 수정할 권한이 없습니다."),


    // 404
    CAREGIVER_NOT_FOUND(HttpStatus.NOT_FOUND, "간병인를 찾을 수 없습니다"),
    PATIENT_NOT_FOUND(HttpStatus.NOT_FOUND, "환자를 찾을 수 없습니다"),

    // 409
    CAREGIVER_PATIENT_CONFLICT(HttpStatus.CONFLICT, "환자와 간병인이 이미 매칭 되어 있습니다"),
    SELF_CARE_MATCHING_NOT_ALLOWED(HttpStatus.CONFLICT, "자신이 등록한 간병인을 자신의 환자로 매칭할 수 없습니다. 또는 그 반대도 불가능합니다."),
    DUPLICATE_JOB_POSTING_AS_PATIENT(HttpStatus.CONFLICT, "이미 환자로서 구인공고를 올렸습니다. 간병인으로 구인공고를 올릴 수 없습니다."),
    DUPLICATE_JOB_POSTING_AS_CAREGIVER(HttpStatus.CONFLICT, "이미 간병인으로서 구인공고를 올렸습니다. 환자로 구인공고를 올릴 수 없습니다."),


    ;

    private final HttpStatus httpStatus;
    private final String message;

    CareApiResponseEnum(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

}
