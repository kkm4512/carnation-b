package com.example.carnation.domain.payment.kakao.dto;

import com.example.carnation.domain.payment.kakao.constans.KakaoPaymentMethodType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 카카오페이 결제 요청 DTO
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class KakaoPaymentReadyRequestDto {

    @Schema(description = "가맹점 코드 (10자)", example = "TC0ONETIME")
    @NotBlank(message = "가맹점 코드(cid)는 필수 입력값입니다.")
    @Size(max = 10, message = "가맹점 코드(cid)는 최대 10자까지 입력 가능합니다.")
    private String cid;

    @Schema(description = "가맹점 코드 인증키 (24자, 옵션)", example = "your_cid_secret", nullable = true)
    @Size(max = 24, message = "가맹점 코드 인증키(cidSecret)는 최대 24자까지 입력 가능합니다.")
    private String cidSecret;

    @Schema(description = "가맹점 주문번호 (최대 100자)", example = "order_123456")
    @NotBlank(message = "가맹점 주문번호(partnerOrderId)는 필수 입력값입니다.")
    @Size(max = 100, message = "가맹점 주문번호(partnerOrderId)는 최대 100자까지 입력 가능합니다.")
    private String partnerOrderId;

    @Schema(description = "가맹점 회원 ID (최대 100자)", example = "user_98765")
    @NotBlank(message = "가맹점 회원 ID(partnerUserId)는 필수 입력값입니다.")
    @Size(max = 100, message = "가맹점 회원 ID(partnerUserId)는 최대 100자까지 입력 가능합니다.")
    private String partnerUserId;

    @Schema(description = "상품명 (최대 100자)", example = "초코파이")
    @NotBlank(message = "상품명(itemName)은 필수 입력값입니다.")
    @Size(max = 100, message = "상품명(itemName)은 최대 100자까지 입력 가능합니다.")
    private String itemName;

    @Schema(description = "상품 코드 (최대 100자, 옵션)", example = "item_001", nullable = true)
    @Size(max = 100, message = "상품 코드(itemCode)는 최대 100자까지 입력 가능합니다.")
    private String itemCode;

    @Schema(description = "상품 수량", example = "1")
    @NotNull(message = "상품 수량(quantity)은 필수 입력값입니다.")
    @Min(value = 1, message = "상품 수량(quantity)은 최소 1개 이상이어야 합니다.")
    private Integer quantity;

    @Schema(description = "상품 총액 (원 단위)", example = "5000")
    @NotNull(message = "상품 총액(totalAmount)은 필수 입력값입니다.")
    @Min(value = 1, message = "상품 총액(totalAmount)은 최소 1원 이상이어야 합니다.")
    private Integer totalAmount;

    @Schema(description = "상품 비과세 금액", example = "0")
    @NotNull(message = "상품 비과세 금액(taxFreeAmount)은 필수 입력값입니다.")
    @Min(value = 0, message = "상품 비과세 금액(taxFreeAmount)은 0원 이상이어야 합니다.")
    private Integer taxFreeAmount;

    @Schema(description = "상품 부가세 금액 (옵션, 기본값: 자동 계산)", example = "500", nullable = true)
    @Min(value = 0, message = "상품 부가세 금액(vatAmount)은 0원 이상이어야 합니다.")
    private Integer vatAmount;

    @Schema(description = "컵 보증금 (옵션)", example = "0")
    @Min(value = 0, message = "컵 보증금(greenDeposit)은 0원 이상이어야 합니다.")
    private Integer greenDeposit;

    @Schema(description = "사용 허가할 결제 수단 (옵션, 기본값: 모든 결제 수단 허용)", example = "CARD", nullable = true)
    private KakaoPaymentMethodType paymentMethodType;

    @Schema(description = "카드 할부 개월 (옵션, 0~12)", example = "0", nullable = true)
    @Min(value = 0, message = "카드 할부 개월(installMonth)은 최소 0개월 이상이어야 합니다.")
    @Max(value = 12, message = "카드 할부 개월(installMonth)은 최대 12개월까지 가능합니다.")
    private Integer installMonth;

    @Schema(description = "분담 무이자 설정 (옵션, Y/N)", example = "N", nullable = true)
    @Pattern(regexp = "Y|N", message = "분담 무이자 설정(useShareInstallment)은 'Y' 또는 'N'만 입력 가능합니다.")
    private String useShareInstallment;

    // ✅ 필수값만 포함하는 생성자
    public KakaoPaymentReadyRequestDto(String cid, String partnerOrderId, String partnerUserId, String itemName, Integer quantity, Integer totalAmount, Integer taxFreeAmount) {
        this.cid = cid;
        this.partnerOrderId = partnerOrderId;
        this.partnerUserId = partnerUserId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
        this.taxFreeAmount = taxFreeAmount;
    }

}
