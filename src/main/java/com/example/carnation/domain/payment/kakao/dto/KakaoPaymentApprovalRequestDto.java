package com.example.carnation.domain.payment.kakao.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 카카오페이 결제 승인 요청 DTO
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class KakaoPaymentApprovalRequestDto {

    @Schema(description = "가맹점 코드 (10자)", example = "TC0ONETIME")
    @NotBlank(message = "가맹점 코드(cid)는 필수 입력값입니다.")
    @Size(max = 10, message = "가맹점 코드(cid)는 최대 10자까지 입력 가능합니다.")
    private String cid;

    @Schema(description = "결제 고유번호, 결제 준비 API 응답에 포함", example = "T1234567890123456789")
    @NotBlank(message = "결제 고유번호(tid)는 필수 입력값입니다.")
    @Size(max = 20, message = "결제 고유번호(tid)는 최대 20자까지 입력 가능합니다.")
    private String tid;

    @Schema(description = "가맹점 코드 인증키 (24자, 옵션)", example = "your_cid_secret", nullable = true)
    @Size(max = 24, message = "가맹점 코드 인증키(cidSecret)는 최대 24자까지 입력 가능합니다.")
    private String cidSecret;

    @Schema(description = "가맹점 주문번호, 결제 준비 API 요청과 일치해야 함", example = "order_123456")
    @NotBlank(message = "가맹점 주문번호(partnerOrderId)는 필수 입력값입니다.")
    @Size(max = 100, message = "가맹점 주문번호(partnerOrderId)는 최대 100자까지 입력 가능합니다.")
    private String partnerOrderId;

    @Schema(description = "가맹점 회원 ID, 결제 준비 API 요청과 일치해야 함", example = "user_98765")
    @NotBlank(message = "가맹점 회원 ID(partnerUserId)는 필수 입력값입니다.")
    @Size(max = 100, message = "가맹점 회원 ID(partnerUserId)는 최대 100자까지 입력 가능합니다.")
    private String partnerUserId;

    @Schema(description = "결제 승인 요청을 인증하는 토큰", example = "pg_token_example")
    @NotBlank(message = "결제 승인 토큰(pgToken)은 필수 입력값입니다.")
    private String pgToken;

    @Schema(description = "결제 승인 요청에 대해 저장하고 싶은 값 (최대 200자)", example = "order_123456789", nullable = true)
    @Size(max = 200, message = "결제 승인 요청 값(payload)은 최대 200자까지 입력 가능합니다.")
    private String payload;

    @Schema(description = "상품 총액 (결제 준비 API 요청과 일치해야 함)", example = "5000", nullable = true)
    @NotNull(message = "상품 총액(totalAmount)은 필수 입력값입니다.")
    @Min(value = 1, message = "상품 총액(totalAmount)은 최소 1원 이상이어야 합니다.")
    private Integer totalAmount;

    // ✅ 필수값만 포함하는 생성자
    public KakaoPaymentApprovalRequestDto(String cid, String tid, String partnerOrderId, String partnerUserId, String pgToken) {
        this.cid = cid;
        this.tid = tid;
        this.partnerOrderId = partnerOrderId;
        this.partnerUserId = partnerUserId;
        this.pgToken = pgToken;
    }
}
