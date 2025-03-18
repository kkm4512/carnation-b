package com.example.carnation.domain.payment.kakao.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KakaoPaymentApprovalResponseDto {
    private String aid;
    private String tid;
    private String cid;
    private String sid;
    private String partner_order_id;
    private String partner_user_id;
    private String payment_method_type;
    private Amount amount;
    private String item_name;
    private CardInfo card_info;
    private int quantity;
    private String created_at;
    private String approved_at;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Amount {
        private int total;
        private int tax_free;
        private int vat;
        private int point;
        private int discount;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class CardInfo {

        @Schema(description = "카카오페이 매입사명", example = "BC카드")
        private String kakaopayPurchaseCorp;

        @Schema(description = "카카오페이 매입사 코드", example = "0001")
        private String kakaopayPurchaseCorpCode;

        @Schema(description = "카카오페이 발급사명", example = "신한카드")
        private String kakaopayIssuerCorp;

        @Schema(description = "카카오페이 발급사 코드", example = "0002")
        private String kakaopayIssuerCorpCode;

        @Schema(description = "카드 BIN", example = "123456")
        private String bin;

        @Schema(description = "카드 타입", example = "신용카드")
        private String cardType;

        @Schema(description = "할부 개월 수", example = "3")
        private String installMonth;

        @Schema(description = "카드사 승인번호", example = "456789123")
        private String approvedId;

        @Schema(description = "카드사 가맹점 번호", example = "M123456789")
        private String cardMid;

        @Schema(description = "무이자할부 여부 (Y/N)", example = "Y")
        private String interestFreeInstall;

        @Schema(description = "할부 유형 (24.02.01부터 제공)", example = "CARD_INSTALLMENT")
        private String installmentType;

        @Schema(description = "카드 상품 코드", example = "C123456")
        private String cardItemCode;
    }
}
