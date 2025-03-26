package com.example.carnation.domain.payment.impl.naver.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NaverPaymentResponseDto {
    private String code;    // 결과 코드
    private String message; // 상세 메시지
    private Object body;         // 실제 응답 데이터 (제네릭 처리)
}
