package com.example.carnation.domain.payment.impl.kakao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(name = "카카오 페이먼트 Simple Response Dto")
public class KakaoPaymentSimpleResponseDto {

    /**
     * 요청한 클라이언트가 모바일 앱일 경우 카카오톡 결제 페이지 Redirect URL
     */
    @JsonProperty("next_redirect_app_url")
    private String nextRedirectAppUrl;

    /**
     * 요청한 클라이언트가 모바일 웹일 경우 카카오톡 결제 페이지 Redirect URL
     */
    @JsonProperty("next_redirect_mobile_url")
    private String nextRedirectMobileUrl;

    /**
     * 요청한 클라이언트가 PC 웹일 경우 카카오톡으로 결제 요청 메시지를 보내기 위한 사용자 정보 입력 화면 Redirect URL
     */
    @JsonProperty("next_redirect_pc_url")
    private String nextRedirectPcUrl;

    /**
     * 카카오페이 결제 화면으로 이동하는 Android 앱 스킴 (내부 서비스용)
     */
    @JsonProperty("android_app_scheme")
    private String androidAppScheme;

    /**
     * 카카오페이 결제 화면으로 이동하는 iOS 앱 스킴 (내부 서비스용)
     */
    @JsonProperty("ios_app_scheme")
    private String iosAppScheme;

    public static KakaoPaymentSimpleResponseDto of (KakaoPaymentReadyResponseDto dto){
        return new KakaoPaymentSimpleResponseDto(
                dto.getNextRedirectAppUrl(),
                dto.getNextRedirectMobileUrl(),
                dto.getNextRedirectPcUrl(),
                dto.getAndroidAppScheme(),
                dto.getIosAppScheme()
        );
    }
}
