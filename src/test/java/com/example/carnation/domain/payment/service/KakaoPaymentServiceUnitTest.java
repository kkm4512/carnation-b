package com.example.carnation.domain.payment.service;

import com.example.carnation.domain.payment.MockPaymentInfo;
import com.example.carnation.domain.payment.kakao.cqrs.KakaoPaymentCommand;
import com.example.carnation.domain.payment.kakao.dto.KakaoPaymentReadyRequestDto;
import com.example.carnation.domain.payment.kakao.dto.KakaoPaymentReadyResponseDto;
import com.example.carnation.domain.payment.kakao.entity.KakaoPayment;
import com.example.carnation.domain.payment.kakao.helper.KakaoPaymentHelper;
import com.example.carnation.domain.payment.kakao.service.KakaoPaymentService;
import com.example.carnation.domain.product.MockProductInfo;
import com.example.carnation.domain.product.cars.ProductQuery;
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
    private KakaoPaymentCommand kakaoPaymentCommand;

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
        KakaoPaymentReadyRequestDto dto = MockPaymentInfo.getKakaoPaymentReadyRequestDto1();
        KakaoPaymentReadyResponseDto mockResponse = MockPaymentInfo.getKakaoPaymentReadyResponse1();
        KakaoPayment mockKakaoPayment = MockPaymentInfo.getKakaoPaymentReady1();
        given(productQuery.readById(1L)).willReturn(MockProductInfo.getProduct1());
        given(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(KakaoPaymentReadyResponseDto.class))).willReturn(ResponseEntity.ok(mockResponse));
        given(kakaoPaymentHelper.getHeadersByKakaoPayment()).willReturn(MockPaymentInfo.getHeadersByKakaoPayment());
        given(kakaoPaymentHelper.getParamsByKakaoPaymentReady(any())).willReturn(MockPaymentInfo.getParamsByKakaoPaymentReady1(mockKakaoPayment));
        given(kakaoPaymentCommand.create(any())).willReturn(mockKakaoPayment);
        given(userQuery.readById(authUser.getUserId())).willReturn(MockUserInfo.getUser1());

        // when
        KakaoPaymentReadyResponseDto response = kakaoPaymentService.ready(authUser,1L);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getTid()).isEqualTo(mockResponse.getTid());
        assertThat(response.getNextRedirectPcUrl()).isEqualTo(mockResponse.getNextRedirectPcUrl());
    }
}
