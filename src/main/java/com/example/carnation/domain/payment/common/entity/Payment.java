package com.example.carnation.domain.payment.common.entity;

import com.example.carnation.domain.order.entity.Order;
import com.example.carnation.domain.payment.common.constans.PaymentGateway;
import com.example.carnation.domain.payment.impl.kakao.constans.KakaoPaymentMethodType;
import com.example.carnation.domain.payment.impl.kakao.constans.KakaoPaymentStatus;
import com.example.carnation.domain.user.common.entity.User;
import com.example.carnation.init.PropertyInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ Ready API 요청 데이터
    @Column(nullable = false, length = 10)
    private String cid;  // 가맹점 코드

    @Column(nullable = false, length = 100)
    private String partnerOrderId;  // 가맹점 주문번호 (고유)

    @Column(nullable = false, length = 100)
    private String partnerUserId;  // 가맹점 회원 ID

    @Column(nullable = false, length = 100)
    private String productName;  // 상품명

    @Column(nullable = false)
    private Integer quantity;  // 상품 수량

    @Column(nullable = false)
    private Integer totalAmount;  // 상품 총액

    @Column(nullable = false)
    private Integer taxFreeAmount;  // 상품 비과세 금액

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentGateway paymentGateway;

    @Column(length = 24)
    private String cidSecret;  // 가맹점 코드 인증키 (옵션)

    @Column(length = 100)
    private String itemCode;  // 상품 코드 (옵션)

    @Column
    private Integer vatAmount;  // 상품 부가세 금액 (옵션)

    @Column
    private Integer greenDeposit;  // 컵 보증금 (옵션)

    @Enumerated(EnumType.STRING)
    private KakaoPaymentMethodType paymentMethod;  // 결제 수단 (CARD / MONEY)

    @Column
    private Integer installMonth;  // 할부 개월

    @Column(length = 1)
    private String useShareInstallment;  // 분담 무이자 설정 (Y/N)

    // ✅ Ready API 응답 데이터
    @Column(unique = true, length = 50)
    private String tid;  // 결제 고유 번호 (Ready API 응답값)

    @Column
    private String pgToken;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private KakaoPaymentStatus paymentStatus;  // 결제 상태

    @CreatedDate
    @Column(updatable = false, nullable = false)
    @Schema(description = "생성일", example = "2024-03-19T12:00:00")
    private LocalDateTime createdAt;  // 결제 준비 요청 시간

    // ✅ User와 ManyToOne 관계 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)  // 외래 키 설정
    private User user;

    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

    public static Payment of(User user, Order entity) {
        UUID uuid = UUID.randomUUID();
        return Payment.builder()
                .cid(PropertyInfo.PAYMENT_KAKAO_MERCHANT_ID)
                .user(user)
                .partnerOrderId(uuid + "_" + entity.getId())
                .partnerUserId(uuid + "_" + user.getId())
                .productName(entity.getProduct().getProductName())
                .quantity(entity.getQuantity())  // 주문 수량
                .totalAmount(entity.getProduct().getPrice() * entity.getQuantity())  // 총액 계산
                .taxFreeAmount(0)
                .paymentGateway(entity.getPaymentGateway())
                .paymentStatus(KakaoPaymentStatus.PENDING)
                .build();
    }


    public void updatePgToken(String pgToken) {
        this.pgToken = pgToken;
    }

    public void updateTid(String tid) {
        this.tid = tid;
    }


    public void updateStatus(KakaoPaymentStatus kakaoPaymentStatus) {
        this.paymentStatus = kakaoPaymentStatus;
    }
}
