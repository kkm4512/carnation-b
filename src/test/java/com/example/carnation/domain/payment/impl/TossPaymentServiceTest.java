package com.example.carnation.domain.payment.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles(profiles = "dev")
class TossPaymentServiceTest {

    @Autowired
    private TossPaymentService tossPaymentService;

    @Test
    @DisplayName("토스 페이먼트 결제 테스트")
    void test1() {
        // 테스트용 요청 객체 생성
        TossTransferRequest request = new TossTransferRequest(
                "INSTANT",
                "KEB_HANA",
                "1234567890",
                10000,
                "테스트 송금",
                "user-1234",
                "order-5678"
        );

        // 서비스 호출
        String s = tossPaymentService.sendMoney(request);
        System.out.println(s);
    }
}
