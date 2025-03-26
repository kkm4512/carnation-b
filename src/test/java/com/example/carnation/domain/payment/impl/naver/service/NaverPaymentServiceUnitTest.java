package com.example.carnation.domain.payment.impl.naver.service;

import com.example.carnation.domain.order.MockOrderInfo;
import com.example.carnation.domain.payment.MockPaymentInfo;
import com.example.carnation.domain.payment.common.cqrs.PaymentCommand;
import com.example.carnation.domain.payment.common.dto.PaymentResponseDto;
import com.example.carnation.domain.payment.common.entity.Payment;
import com.example.carnation.domain.payment.impl.naver.dto.NaverPaymentResponseDto;
import com.example.carnation.domain.payment.impl.naver.helper.NaverPaymentHelper;
import com.example.carnation.domain.user.MockUserInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class NaverPaymentServiceUnitTest {

    @InjectMocks
    private NaverPaymentService naverPaymentService; // 실제 테스트할 서비스

    @Mock
    NaverPaymentHelper naverPaymentHelper;

    @Mock
    PaymentCommand paymentCommand;

    @Mock
    RestTemplate restTemplate;

    @Test
    @DisplayName("네이버 페이먼트 결제 준비 테스트")
    void test1() {
        // given
        NaverPaymentResponseDto mockResponse = MockPaymentInfo.getNaverPaymentResponse1();
        Payment mockPayment = MockPaymentInfo.getPayment1();
        given(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(NaverPaymentResponseDto.class))).willReturn(ResponseEntity.ok(mockResponse));
        given(naverPaymentHelper.getHeadersByNaverPayment()).willReturn(MockPaymentInfo.getHeadersByKakaoPayment());
        given(naverPaymentHelper.getParamsByNaverPayment(any())).willReturn(MockPaymentInfo.getParamsByKakaoPaymentReady1(mockPayment));
        given(paymentCommand.create(any())).willReturn(mockPayment);

        // when
        PaymentResponseDto response = naverPaymentService.ready(MockUserInfo.getUser1(), MockOrderInfo.getOrder1());

        // then
        assertThat(response).isNotNull();
        assertThat(response.getMessage()).isEqualTo(mockResponse.getMessage());
    }

}