package com.example.carnation.domain.payment.kakao.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(name = "카카오 페이먼트 Ready Response Dto")
public class KakaoPaymentReadyResponseDto {

    /**
     * 결제 고유 번호 (20자)
     */
    private String tid;

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

    /**
     * 결제 준비 요청 시간
     */
    /** 결제 준비 요청 시간 (ISO 8601 형식) */
    @JsonProperty("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
}
