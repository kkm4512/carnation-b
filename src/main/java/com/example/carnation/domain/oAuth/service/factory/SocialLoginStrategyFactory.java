package com.example.carnation.domain.oAuth.service.factory;

import com.example.carnation.common.exception.OAuthException;
import com.example.carnation.domain.oAuth.dto.OAuthProviderDto;
import com.example.carnation.domain.oAuth.service.impl.GoogleLoginServiceImpl;
import com.example.carnation.domain.oAuth.service.impl.KakaoLoginServiceImpl;
import com.example.carnation.domain.oAuth.service.impl.NaverLoginServiceImpl;
import com.example.carnation.domain.oAuth.service.interfaces.SocialLoginService;
import com.example.carnation.init.StaticProperties;
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

    public SocialLoginService getLoginService(OAuthProviderDto oAuthProviderDto) {
        switch (oAuthProviderDto.getOAuthProviderName()) {
            case KAKAO:
                return new KakaoLoginServiceImpl(oAuthProviderDto, StaticProperties.getKakaoClientId(),restTemplate, objectMapper);
            case NAVER:
                return new NaverLoginServiceImpl(oAuthProviderDto, StaticProperties.getNaverClientId(), StaticProperties.getNaverClientSecret(),restTemplate, objectMapper);
            case GOOGLE:
                return new GoogleLoginServiceImpl(oAuthProviderDto, StaticProperties.getGoogleClientId(), StaticProperties.getGoogleClientSecret(),restTemplate, objectMapper);
            default:
                throw new OAuthException(OAUTH_NOT_FOUND_PROVIDER);
        }
    }
}