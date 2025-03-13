package com.example.carnation.init;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OAuthProperties {

    @Value("${social.redirect.url}")
    private String redirectUrl;

    @Value("${social.kakao.client-id}")
    private String kakaoClientId;

    @Value("${social.naver.client-id}")
    private String naverClientId;

    @Value("${social.naver.client-secret}")
    private String naverClientSecret;

    @Value("${social.google.client-id}")
    private String googleClientId;

    @Value("${social.google.client-secret}")
    private String googleClientSecret;

    private static String STATIC_REDIRECT_URL;
    private static String STATIC_KAKAO_CLIENT_ID;
    private static String STATIC_NAVER_CLIENT_ID;
    private static String STATIC_NAVER_CLIENT_SECRET;
    private static String STATIC_GOOGLE_CLIENT_ID;
    private static String STATIC_GOOGLE_CLIENT_SECRET;

    @PostConstruct
    public void init() {
        STATIC_REDIRECT_URL = redirectUrl;
        STATIC_KAKAO_CLIENT_ID = kakaoClientId;
        STATIC_NAVER_CLIENT_ID = naverClientId;
        STATIC_NAVER_CLIENT_SECRET = naverClientSecret;
        STATIC_GOOGLE_CLIENT_ID = googleClientId;
        STATIC_GOOGLE_CLIENT_SECRET = googleClientSecret;
    }

    public static String getRedirectUrl() {
        return STATIC_REDIRECT_URL;
    }

    public static String getKakaoClientId() {
        return STATIC_KAKAO_CLIENT_ID;
    }

    public static String getNaverClientId() {
        return STATIC_NAVER_CLIENT_ID;
    }

    public static String getNaverClientSecret() {
        return STATIC_NAVER_CLIENT_SECRET;
    }

    public static String getGoogleClientId() {
        return STATIC_GOOGLE_CLIENT_ID;
    }

    public static String getGoogleClientSecret() {
        return STATIC_GOOGLE_CLIENT_SECRET;
    }
}
