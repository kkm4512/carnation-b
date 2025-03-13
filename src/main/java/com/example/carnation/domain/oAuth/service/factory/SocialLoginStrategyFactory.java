package com.example.carnation.domain.oAuth.service.factory;

import com.example.carnation.common.exception.OAuthException;
import com.example.carnation.domain.oAuth.dto.OAuthProviderDto;
import com.example.carnation.domain.oAuth.service.impl.GoogleStrategy;
import com.example.carnation.domain.oAuth.service.impl.KakaoStrategy;
import com.example.carnation.domain.oAuth.service.impl.NaverStrategy;
import com.example.carnation.domain.oAuth.service.interfaces.SocialLoginStrategy;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import static com.example.carnation.common.response.enums.OAuthApiResponse.OAUTH_NOT_FOUND_PROVIDER;

@Component
@RequiredArgsConstructor
public class SocialLoginStrategyFactory {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

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


    public SocialLoginStrategy getStrategy(OAuthProviderDto oAuthProviderDto) {
        switch (oAuthProviderDto.getOAuthProviderName()) {
            case KAKAO:
                return new KakaoStrategy(oAuthProviderDto,kakaoClientId,restTemplate, objectMapper);
            case NAVER:
                return new NaverStrategy(oAuthProviderDto,naverClientId, naverClientSecret,restTemplate, objectMapper);
            case GOOGLE:
                return new GoogleStrategy(oAuthProviderDto,googleClientId, googleClientSecret,restTemplate, objectMapper);
            default:
                throw new OAuthException(OAUTH_NOT_FOUND_PROVIDER);
        }
    }
}