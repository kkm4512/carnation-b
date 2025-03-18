package com.example.carnation.domain.payment.kakao.entity;

import com.example.carnation.domain.payment.kakao.constans.KakaoPaymentMethodType;
import com.example.carnation.domain.payment.kakao.dto.KakaoPaymentReadyRequestDto;
import com.example.carnation.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "kakao_payment_ready")
public class KakaoPaymentReady {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ Ready API 요청 데이터
    @Column(nullable = false, length = 10)
    private String cid;  // 가맹점 코드

    @Column(length = 24)
    private String cidSecret;  // 가맹점 코드 인증키 (옵션)

    @Column(nullable = false, length = 100)
    private String partnerOrderId;  // 가맹점 주문번호 (고유)

    @Column(nullable = false, length = 100)
    private String partnerUserId;  // 가맹점 회원 ID

    @Column(nullable = false, length = 100)
    private String itemName;  // 상품명

    @Column(length = 100)
    private String itemCode;  // 상품 코드 (옵션)

    @Column(nullable = false)
    private Integer quantity;  // 상품 수량

    @Column(nullable = false)
    private Integer totalAmount;  // 상품 총액

    @Column(nullable = false)
    private Integer taxFreeAmount;  // 상품 비과세 금액

    @Column
    private Integer vatAmount;  // 상품 부가세 금액 (옵션)

    @Column
    private Integer greenDeposit;  // 컵 보증금 (옵션)

    @Enumerated(EnumType.STRING)
    private KakaoPaymentMethodType paymentMethodType;  // 결제 수단 (CARD / MONEY)

    @Column
    private Integer installMonth;  // 할부 개월

    @Column(length = 1)
    private String useShareInstallment;  // 분담 무이자 설정 (Y/N)

    // ✅ Ready API 응답 데이터
    @Column(unique = true, length = 50)
    private String tid;  // 결제 고유 번호 (Ready API 응답값)

    @Column
    private String pgToken;

    @Column
    private LocalDateTime createdAt;  // 결제 준비 요청 시간

    // ✅ User와 ManyToOne 관계 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)  // 외래 키 설정
    private User user;

    public KakaoPaymentReady(User user, Long id, String cid, String cidSecret, String partnerOrderId, String partnerUserId, String itemName, String itemCode, Integer quantity, Integer totalAmount, Integer taxFreeAmount, Integer vatAmount, Integer greenDeposit, KakaoPaymentMethodType paymentMethodType, Integer installMonth, String useShareInstallment) {
        this.id = id;
        this.cid = cid;
        this.cidSecret = cidSecret;
        this.partnerOrderId = partnerOrderId;
        this.partnerUserId = partnerUserId;
        this.itemName = itemName;
        this.itemCode = itemCode;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
        this.taxFreeAmount = taxFreeAmount;
        this.vatAmount = vatAmount;
        this.greenDeposit = greenDeposit;
        this.paymentMethodType = paymentMethodType;
        this.installMonth = installMonth;
        this.useShareInstallment = useShareInstallment;
        this.user = user;
    }

    public static KakaoPaymentReady of(User user, KakaoPaymentReadyRequestDto req) {
        return new KakaoPaymentReady(
                user,
                null, // id (자동 생성)
                req.getCid(),
                req.getCidSecret(),
                req.getPartnerOrderId(),
                req.getPartnerUserId(),
                req.getItemName(),
                req.getItemCode(),
                req.getQuantity(),
                req.getTotalAmount(),
                req.getTaxFreeAmount(),
                req.getVatAmount(),
                req.getGreenDeposit(),
                req.getPaymentMethodType(), // Enum -> String 변환
                req.getInstallMonth(),
                req.getUseShareInstallment()
        );
    }

    public void updatePgToken(String pgToken) {
        this.pgToken = pgToken;
    }

    public void updateTid(String tid) {
        this.tid = tid;
    }

    public void updateCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }


}
