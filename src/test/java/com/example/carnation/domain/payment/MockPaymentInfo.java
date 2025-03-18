package com.example.carnation.domain.payment;

import com.example.carnation.domain.payment.kakao.dto.KakaoPaymentReadyRequestDto;
import com.example.carnation.domain.payment.kakao.dto.KakaoPaymentReadyResponseDto;
import com.example.carnation.domain.payment.kakao.entity.KakaoPaymentReady;
import com.example.carnation.domain.user.MockUserTestInfo;

import java.time.LocalDateTime;

public class MockPaymentInfo {

    /**
     * 필수값만 포함된 Mock 결제 정보 생성
     */
    public static KakaoPaymentReadyRequestDto getKakaoPaymentReadyRequestDto1() {
        return new KakaoPaymentReadyRequestDto(
                "TC0ONETIME", // 가맹점 코드
                "mock_order_123456", // 가맹점 주문번호
                "mock_user_98765", // 가맹점 회원 ID
                "테스트 상품", // 상품명
                1, // 상품 수량
                5000, // 상품 총액
                0 // 상품 비과세 금액
        );
    }

    public static KakaoPaymentReadyRequestDto getKakaoPaymentReadyRequestRequiredDtoFail1() {
        return new KakaoPaymentReadyRequestDto(
                "TC0ONETIME", // 가맹점 코드
                "mock_order_123456", // 가맹점 주문번호
                "mock_user_98765", // 가맹점 회원 ID
                "테스트 상품", // 상품명
                1, // 상품 수량
                null, // 상품 총액
                0 // 상품 비과세 금액
        );
    }

    /**
     * 카카오페이 결제 준비 API 응답 (필수값 + 선택값 포함)
     */
    public static KakaoPaymentReadyResponseDto getKakaoPaymentReadyResponse1() {
        return new KakaoPaymentReadyResponseDto(
                "T09876543210987654321", // 결제 고유 번호
                "https://mock.kakaopay.com/redirect/app", // 모바일 앱 리디렉트 URL
                "https://mock.kakaopay.com/redirect/mobile", // 모바일 웹 리디렉트 URL
                "https://mock.kakaopay.com/redirect/pc", // PC 웹 리디렉트 URL
                "kakaopay://app", // Android 앱 스킴
                "kakaopay://ios", // iOS 앱 스킴
                LocalDateTime.now()
        );
    }

    public static KakaoPaymentReady getKakaoPaymentReady1() {
        return KakaoPaymentReady.of(
                MockUserTestInfo.getUser1(),
                getKakaoPaymentReadyRequestDto1()
        );
    }
}
