package com.example.carnation.domain.payment.service;

import com.example.carnation.domain.order.MockOrderInfo;
import com.example.carnation.domain.payment.MockPaymentInfo;
import com.example.carnation.domain.payment.impl.kakao.cqrs.PaymentCommand;
import com.example.carnation.domain.payment.impl.kakao.dto.KakaoPaymentRequestDto;
import com.example.carnation.domain.payment.impl.kakao.dto.KakaoPaymentResponseDto;
import com.example.carnation.domain.payment.common.entity.Payment;
import com.example.carnation.domain.payment.impl.kakao.helper.KakaoPaymentHelper;
import com.example.carnation.domain.payment.impl.kakao.service.KakaoPaymentService;
import com.example.carnation.domain.product.MockProductInfo;
import com.example.carnation.domain.product.cqrs.ProductQuery;
import com.example.carnation.domain.user.MockUserInfo;
import com.example.carnation.domain.user.common.cqrs.UserQuery;
import com.example.carnation.security.AuthUser;
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
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class KakaoPaymentServiceUnitTest {

    @InjectMocks
    private KakaoPaymentService kakaoPaymentService; // 실제 테스트할 서비스

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private PaymentCommand paymentCommand;

    @Mock
    private KakaoPaymentHelper kakaoPaymentHelper;

    @Mock
    private UserQuery userQuery;

    @Mock
    private ProductQuery productQuery;


    @Test
    @DisplayName("카카오 페이먼트 결제 준비 테스트")
    void test1() {
        // given
        AuthUser authUser = MockUserInfo.getAuthUser1();
        KakaoPaymentResponseDto mockResponse = MockPaymentInfo.getKakaoPaymentReadyResponse1();
        Payment mockPayment = MockPaymentInfo.getPayment1();
        given(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(KakaoPaymentResponseDto.class))).willReturn(ResponseEntity.ok(mockResponse));
        given(kakaoPaymentHelper.getHeadersByKakaoPayment()).willReturn(MockPaymentInfo.getHeadersByKakaoPayment());
        given(kakaoPaymentHelper.getParamsByKakaoPayment(any())).willReturn(MockPaymentInfo.getParamsByKakaoPaymentReady1(mockPayment));
        given(paymentCommand.create(any())).willReturn(mockPayment);

        // when
        KakaoPaymentResponseDto response = kakaoPaymentService.ready(MockUserInfo.getUser1(), MockOrderInfo.getOrder1());

        // then
        assertThat(response).isNotNull();
        assertThat(response.getTid()).isEqualTo(mockResponse.getTid());
        assertThat(response.getNextRedirectPcUrl()).isEqualTo(mockResponse.getNextRedirectPcUrl());
    }
}
