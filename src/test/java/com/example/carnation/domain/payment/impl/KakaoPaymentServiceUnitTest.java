package com.example.carnation.domain.payment.impl;

import com.example.carnation.domain.payment.MockPaymentInfo;
import com.example.carnation.domain.payment.kakao.cqrs.KakaoPaymentCommand;
import com.example.carnation.domain.payment.kakao.dto.KakaoPaymentReadyRequestDto;
import com.example.carnation.domain.payment.kakao.dto.KakaoPaymentReadyResponseDto;
import com.example.carnation.domain.payment.kakao.entity.KakaoPaymentReady;
import com.example.carnation.domain.payment.kakao.service.KakaoPaymentService;
import com.example.carnation.domain.user.MockUserTestInfo;
import com.example.carnation.domain.user.cqrs.UserQuery;
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
    private UserQuery userQuery;

    @Test
    @DisplayName("카카오 페이먼트 결제 준비 테스트")
    void test1() {
        // given
        AuthUser authUser = MockUserTestInfo.getAuthUser1();
        KakaoPaymentReadyRequestDto dto = MockPaymentInfo.getKakaoPaymentReadyRequestDto1();
        KakaoPaymentReadyResponseDto mockResponse = MockPaymentInfo.getKakaoPaymentReadyResponse1();
        KakaoPaymentReady mockKakaoPayment = MockPaymentInfo.getKakaoPaymentReady1();
        given(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(KakaoPaymentReadyResponseDto.class))).willReturn(ResponseEntity.ok(mockResponse));
        given(kakaoPaymentCommand.create(any())).willReturn(mockKakaoPayment);
        given(userQuery.readById(authUser.getUserId())).willReturn(MockUserTestInfo.getUser1());

        // when
        KakaoPaymentReadyResponseDto response = kakaoPaymentService.ready(authUser,dto);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getTid()).isEqualTo(mockResponse.getTid());
        assertThat(response.getNextRedirectPcUrl()).isEqualTo(mockResponse.getNextRedirectPcUrl());
    }
}
