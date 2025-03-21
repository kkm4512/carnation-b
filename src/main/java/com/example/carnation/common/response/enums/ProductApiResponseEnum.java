package com.example.carnation.common.response.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ProductApiResponseEnum implements ApiResponseEnum {

    // 400 Bad Request (잘못된 요청)
    INVALID_PRODUCT_NAME(HttpStatus.BAD_REQUEST, "상품명이 유효하지 않습니다."),
    INVALID_PRICE(HttpStatus.BAD_REQUEST, "가격은 0보다 커야 합니다."),
    INVALID_QUANTITY(HttpStatus.BAD_REQUEST, "재고 수량은 0 이상이어야 합니다."),

    // 403 Forbidden (권한 없음)
    UNAUTHORIZED_ACCESS(HttpStatus.FORBIDDEN, "해당 상품을 수정 또는 삭제할 권한이 없습니다."),

    // 404 Not Found (리소스를 찾을 수 없음)
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "요청하신 상품을 찾을 수 없습니다."),

    // 409 Conflict (중복 요청, 충돌)
    DUPLICATE_PRODUCT_NAME(HttpStatus.CONFLICT, "이미 동일한 상품명이 존재합니다."),
    STOCK_CONFLICT(HttpStatus.CONFLICT, "재고가 부족하여 주문을 처리할 수 없습니다."),
    INSUFFICIENT_STOCK(HttpStatus.CONFLICT, "요청 수량이 재고보다 많습니다."), // ✅ 추가

    // 410 Gone (만료된 리소스)
    EXPIRED_VERIFICATION_CODE(HttpStatus.GONE, "인증 코드가 만료되었습니다. 다시 요청해주세요."),

    // 500 Internal Server Error (서버 내부 오류)
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다. 관리자에게 문의하세요.");

    private final HttpStatus httpStatus;
    private final String message;

    ProductApiResponseEnum(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
