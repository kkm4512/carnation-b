package com.example.carnation.common.response.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum PaymentApiResponseEnum implements ApiResponseEnum {

    // 400 Bad Request (잘못된 요청)
    INVALID_PAYMENT_AMOUNT(HttpStatus.BAD_REQUEST, "잘못된 결제 금액입니다."),
    MISSING_PAYMENT_DETAILS(HttpStatus.BAD_REQUEST, "결제에 필요한 세부 정보가 누락되었습니다."),
    PAYMENT_METHOD_NOT_SUPPORTED(HttpStatus.BAD_REQUEST, "지원되지 않는 결제 방법입니다."),

    // 403 Forbidden
    INSUFFICIENT_BALANCE(HttpStatus.FORBIDDEN, "잔액이 부족하여 결제할 수 없습니다."),
    PAYMENT_METHOD_NOT_AUTHORIZED(HttpStatus.FORBIDDEN, "결제 방법이 승인되지 않았습니다."),

    // 404 Not Found
    PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "결제를 찾을 수 없습니다."),
    TRANSACTION_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 결제 거래를 찾을 수 없습니다."),

    // 409 Conflict
    PAYMENT_ALREADY_PROCESSED(HttpStatus.CONFLICT, "이미 처리된 결제입니다."),
    DUPLICATE_PAYMENT(HttpStatus.CONFLICT, "중복된 결제 요청입니다."),

    // 410 Gone (만료)
    EXPIRED_VERIFICATION_CODE(HttpStatus.GONE, "인증 코드가 만료되었습니다. 다시 요청해주세요."),

    // 500 Internal Server Error (서버 내부 오류)
    PAYMENT_PROCESSING_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "결제 처리에 실패했습니다."),
    PAYMENT_GATEWAY_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "결제 게이트웨이에서 오류가 발생했습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    PaymentApiResponseEnum(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
