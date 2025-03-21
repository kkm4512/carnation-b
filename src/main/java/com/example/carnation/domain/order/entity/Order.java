package com.example.carnation.domain.order.entity;

import com.example.carnation.domain.order.dto.OrderRequestDto;
import com.example.carnation.domain.payment.common.constans.PaymentGateway;
import com.example.carnation.domain.payment.common.entity.Payment;
import com.example.carnation.domain.product.entity.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 주문 ID

    @ManyToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;  // 결제 정보 (N:1 관계)

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;  // 결제 정보 (N:1 관계)

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentGateway paymentGateway;

    @Column(nullable = false)
    private Boolean isPaid;     // 결제 상태

    @Column(nullable = false)
    private Integer quantity;  // 주문 수량

    @Column(length = 255, nullable = false)
    private String address;  // 배송 주소

    @Column(length = 50)
    private String receiverName;  // 수령인 이름

    @Column(length = 20)
    private String phoneNumber;  // 수령인 연락처

    @Column(length = 255)
    private String orderMemo;  // 배송 요청사항

    @Column(length = 50)
    private String couponCode;  // 할인 쿠폰 코드

    @CreatedDate
    @Column(updatable = false, nullable = false)
    @Schema(description = "생성일", example = "2024-03-19T12:00:00")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    @Schema(description = "수정일", example = "2024-03-20T15:30:00")
    private LocalDateTime updatedAt;

    // isPaid false 설정 잊지말기
    public static Order of(Product product, OrderRequestDto dto) {
        return Order.builder()
                .product(product)
                .paymentGateway(dto.getPaymentGateway())
                .isPaid(false)
                .quantity(dto.getQuantity())
                .address(dto.getAddress())
                .receiverName(product.getUser().getNickname() == null ? null : product.getUser().getNickname())
                .phoneNumber(product.getUser().getPhoneNumber() == null ? null : product.getUser().getPhoneNumber())
                .orderMemo(dto.getOrderMemo() == null ? null : dto.getOrderMemo())
                .couponCode(dto.getCouponCode() == null ? null : dto.getCouponCode())
                .build();
    }

    public void updatePayment(Payment payment) {
        this.payment = payment;
    }

    public void updateIsPaid(Boolean isPaid) {
        this.isPaid = isPaid;
    }
}
