package com.example.carnation.common.response.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum OrderApiResponseEnum implements ApiResponseEnum {

    // 400 Bad Request (잘못된 요청)
    INVALID_ORDER_AMOUNT(HttpStatus.BAD_REQUEST, "주문 금액이 유효하지 않습니다."),
    INVALID_PAYMENT_METHOD(HttpStatus.BAD_REQUEST, "지원되지 않는 결제 방식입니다."),
    INVALID_ORDER_STATUS(HttpStatus.BAD_REQUEST, "잘못된 주문 상태 값입니다."),
    INVALID_USER(HttpStatus.BAD_REQUEST, "유효하지 않은 사용자입니다."),

    // 403 Forbidden (권한 없음)
    UNAUTHORIZED_ORDER_ACCESS(HttpStatus.FORBIDDEN, "해당 주문을 수정 또는 삭제할 권한이 없습니다."),

    // 404 Not Found (리소스를 찾을 수 없음)
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "요청하신 주문을 찾을 수 없습니다."),
    PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 주문의 결제 정보를 찾을 수 없습니다."),

    // 409 Conflict (중복 요청, 충돌)
    DUPLICATE_ORDER(HttpStatus.CONFLICT, "이미 동일한 주문이 존재합니다."),
    ORDER_ALREADY_PAID(HttpStatus.CONFLICT, "해당 주문은 이미 결제 완료되었습니다."),
    ORDER_STATUS_CONFLICT(HttpStatus.CONFLICT, "주문의 현재 상태에서는 해당 작업을 수행할 수 없습니다."),

    // 410 Gone (만료된 리소스)
    EXPIRED_ORDER(HttpStatus.GONE, "해당 주문은 취소되었거나 유효하지 않습니다."),
    PAYMENT_EXPIRED(HttpStatus.GONE, "결제 유효 시간이 만료되었습니다. 다시 시도해주세요."),

    // 500 Internal Server Error (서버 내부 오류)
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다. 관리자에게 문의하세요.");

    private final HttpStatus httpStatus;
    private final String message;

    OrderApiResponseEnum(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
