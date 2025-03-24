package com.example.carnation.domain.user.oAuth.service;

import com.example.carnation.annotation.RotateToken;
import com.example.carnation.domain.user.auth.helper.TokenHelper;
import com.example.carnation.domain.user.common.cqrs.UserCommand;
import com.example.carnation.domain.user.common.cqrs.UserQuery;
import com.example.carnation.domain.user.common.entity.User;
import com.example.carnation.domain.user.oAuth.constans.OAuthProviderNameType;
import com.example.carnation.domain.user.oAuth.dto.OAuthProviderDto;
import com.example.carnation.domain.user.oAuth.service.factory.SocialLoginStrategyFactory;
import com.example.carnation.domain.user.oAuth.service.interfaces.SocialLoginService;
import com.example.carnation.security.TokenDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j(topic = "OAuthService")
@RequiredArgsConstructor
public class OAuthService {
    private final SocialLoginStrategyFactory factory;
    private final UserQuery userQuery;
    private final UserCommand userCommand;
    private final TokenHelper tokenHelper;

    @RotateToken
    public TokenDto socialLogin(final OAuthProviderNameType oAuthProviderName, final String code) {
        OAuthProviderDto oAuthProviderDto = OAuthProviderDto.of(oAuthProviderName,code);
        SocialLoginService socialLoginService = factory.getLoginService(oAuthProviderDto);
        String socialAccessToken = socialLoginService.getAccessToken(oAuthProviderDto);
        oAuthProviderDto.updateAccessToken(socialAccessToken);
        User user = socialLoginService.getUser(oAuthProviderDto);
        // 중복된 이메일이 없다면 false 반환
        boolean flag = userQuery.existsByEmail(user.getEmail());
        if (!flag) {
            User saveUser = userCommand.create(user);
            return tokenHelper.createTokenDto(saveUser);
        }
        else {
            User findUser = userQuery.readByEmail(user.getEmail());
            return tokenHelper.createTokenDto(findUser);
        }
    }
}
