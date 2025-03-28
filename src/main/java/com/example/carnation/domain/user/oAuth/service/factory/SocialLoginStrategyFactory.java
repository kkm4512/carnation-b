package com.example.carnation.domain.user.oAuth.service.factory;

import com.example.carnation.common.exception.OAuthException;
import com.example.carnation.domain.user.oAuth.dto.OAuthProviderDto;
import com.example.carnation.domain.user.oAuth.service.impl.GoogleLoginServiceImpl;
import com.example.carnation.domain.user.oAuth.service.impl.KakaoLoginServiceImpl;
import com.example.carnation.domain.user.oAuth.service.impl.NaverLoginServiceImpl;
import com.example.carnation.domain.user.oAuth.service.interfaces.SocialLoginService;
import com.example.carnation.init.PropertyInfo;
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
        switch (oAuthProviderDto.getNameEnum()) {
            case KAKAO:
                return new KakaoLoginServiceImpl(oAuthProviderDto, PropertyInfo.KAKAO_CLIENT_ID,restTemplate, objectMapper);
            case NAVER:
                return new NaverLoginServiceImpl(oAuthProviderDto, PropertyInfo.NAVER_CLIENT_ID, PropertyInfo.NAVER_CLIENT_SECRET,restTemplate, objectMapper);
            case GOOGLE:
                return new GoogleLoginServiceImpl(oAuthProviderDto, PropertyInfo.GOOGLE_CLIENT_ID, PropertyInfo.GOOGLE_CLIENT_SECRET,restTemplate, objectMapper);
            default:
                throw new OAuthException(OAUTH_NOT_FOUND_PROVIDER);
        }
    }
}