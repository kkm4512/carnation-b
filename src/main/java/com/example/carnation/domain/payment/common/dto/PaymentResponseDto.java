package com.example.carnation.domain.payment.common.dto;

import com.example.carnation.domain.payment.impl.kakao.constans.PaymentStatus;
import com.example.carnation.domain.payment.impl.kakao.dto.KakaoPaymentReadyResponseDto;
import com.example.carnation.domain.payment.impl.naver.dto.NaverPaymentResponseDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "Payment Response Dto")
public class PaymentResponseDto {

    // 카카오 페이
    private String nextRedirectAppUrl;
    private String nextRedirectMobileUrl;
    private String nextRedirectPcUrl;
    private String androidAppScheme;
    private String iosAppScheme;

    // 네이버 페이
    private String code;    // 결과 코드
    private String message; // 상세 메시지
    private Object body;


    // 공통
    @Schema(name = "현재 페이 상태값")
    private PaymentStatus PaymentStatus;

    // 카카오페이먼트 -> 페이먼트
    public PaymentResponseDto(String nextRedirectAppUrl, String nextRedirectMobileUrl, String nextRedirectPcUrl, String androidAppScheme, String iosAppScheme) {
        this.nextRedirectAppUrl = nextRedirectAppUrl;
        this.nextRedirectMobileUrl = nextRedirectMobileUrl;
        this.nextRedirectPcUrl = nextRedirectPcUrl;
        this.androidAppScheme = androidAppScheme;
        this.iosAppScheme = iosAppScheme;
    }

    public PaymentResponseDto(String code, String message, Object body) {
        this.code = code;
        this.message = message;
        this.body = body;
    }

    // 카카오페이먼트 -> 페이먼트
    public static PaymentResponseDto of (KakaoPaymentReadyResponseDto dto){
        return new PaymentResponseDto(
                dto.getNextRedirectAppUrl(),
                dto.getNextRedirectMobileUrl(),
                dto.getNextRedirectPcUrl(),
                dto.getAndroidAppScheme(),
                dto.getIosAppScheme()
        );
    }

    public static PaymentResponseDto of(NaverPaymentResponseDto dto) {
        return new PaymentResponseDto(
                dto.getCode(),
                dto.getMessage(),
                dto.getBody()
        );
    }
}
