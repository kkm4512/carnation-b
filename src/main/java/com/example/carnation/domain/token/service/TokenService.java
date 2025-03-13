package com.example.carnation.domain.token.service;

import com.example.carnation.common.service.RedisService;
import com.example.carnation.domain.token.dto.TokenRefreshRequestDto;
import com.example.carnation.domain.user.cqrs.UserQuery;
import com.example.carnation.domain.user.entity.User;
import com.example.carnation.security.JwtDto;
import com.example.carnation.security.JwtManager;
import com.example.carnation.security.TokenDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "AuthService")
public class TokenService {
    private final RedisService redisService;
    private final JwtManager jwtManager;
    private final UserQuery userQuery;

    public TokenDto refreshAccessToken(TokenRefreshRequestDto dto) {
        jwtManager.validateRefreshToken(dto.getRefreshToken());
        String refreshToken = redisService.getRefreshToken(dto.getUserId());
        jwtManager.compare(dto.getRefreshToken(),refreshToken);
        User user = userQuery.findById(dto.getUserId());
        String accessToken = jwtManager.generateAccessToken(JwtDto.of(user));
        return TokenDto.of(
                user.getId(),
                accessToken,
                refreshToken
        );

    }
}
