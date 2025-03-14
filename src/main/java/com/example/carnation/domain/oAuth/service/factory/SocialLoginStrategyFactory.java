package com.example.carnation.domain.oAuth.service.factory;

import com.example.carnation.common.exception.OAuthException;
import com.example.carnation.domain.oAuth.dto.OAuthProviderDto;
import com.example.carnation.domain.oAuth.service.impl.GoogleStrategy;
import com.example.carnation.domain.oAuth.service.impl.KakaoStrategy;
import com.example.carnation.domain.oAuth.service.impl.NaverStrategy;
import com.example.carnation.domain.oAuth.service.interfaces.SocialLoginStrategy;
import com.example.carnation.init.OAuthProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import static com.example.carnation.common.response.enums.OAuthApiResponseEnum.OAUTH_NOT_FOUND_PROVIDER;

@Component
@RequiredArgsConstructor
public class SocialLoginStrategyFactory {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public SocialLoginStrategy getStrategy(OAuthProviderDto oAuthProviderDto) {
        switch (oAuthProviderDto.getOAuthProviderName()) {
            case KAKAO:
                return new KakaoStrategy(oAuthProviderDto, OAuthProperties.getKakaoClientId(),restTemplate, objectMapper);
            case NAVER:
                return new NaverStrategy(oAuthProviderDto,OAuthProperties.getNaverClientId(), OAuthProperties.getNaverClientSecret(),restTemplate, objectMapper);
            case GOOGLE:
                return new GoogleStrategy(oAuthProviderDto,OAuthProperties.getGoogleClientId(), OAuthProperties.getGoogleClientSecret(),restTemplate, objectMapper);
            default:
                throw new OAuthException(OAUTH_NOT_FOUND_PROVIDER);
        }
    }
}