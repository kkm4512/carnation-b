package com.example.carnation.domain.oAuth.service;

import com.example.carnation.annotation.SaveRefreshToken;
import com.example.carnation.domain.oAuth.constans.OAuthProviderName;
import com.example.carnation.domain.oAuth.dto.OAuthProviderDto;
import com.example.carnation.domain.oAuth.service.factory.SocialLoginStrategyFactory;
import com.example.carnation.domain.oAuth.service.interfaces.SocialLoginStrategy;
import com.example.carnation.domain.token.service.TokenService;
import com.example.carnation.domain.user.cqrs.UserCommand;
import com.example.carnation.domain.user.cqrs.UserQuery;
import com.example.carnation.domain.user.entity.User;
import com.example.carnation.security.JwtManager;
import com.example.carnation.security.TokenDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j(topic = "OAuthService")
@RequiredArgsConstructor
public class OAuthService {
    private final SocialLoginStrategyFactory factory;
    private final JwtManager jwtManager;
    private final UserQuery userQuery;
    private final UserCommand userCommand;
    private final TokenService tokenService;

    @SaveRefreshToken
    public TokenDto socialLogin(final OAuthProviderName oAuthProviderName, final String code) {
        OAuthProviderDto oAuthProviderDto = OAuthProviderDto.of(oAuthProviderName,code);
        SocialLoginStrategy strategy = factory.getStrategy(oAuthProviderDto);
        String socialAccessToken = strategy.getAccessToken(oAuthProviderDto);
        oAuthProviderDto.updateAccessToken(socialAccessToken);
        User user = strategy.getUser(oAuthProviderDto);
        // 중복된 이메일이 없다면 false 반환
        boolean flag = userQuery.existsByEmail(user.getEmail());
        if (!flag) {
            User saveUser = userCommand.create(user);
            return tokenService.createTokenDto(saveUser);
        }
        else {
            User findUser = userQuery.readByEmail(user.getEmail());
            return tokenService.createTokenDto(findUser);
        }
    }
}
