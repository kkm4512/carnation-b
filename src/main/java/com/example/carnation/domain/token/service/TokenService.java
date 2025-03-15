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

    public TokenDto refreshAccessToken(final TokenRefreshRequestDto dto) {
        jwtManager.validateRefreshToken(dto.getRefreshToken());
        String refreshToken = redisService.getRefreshToken(dto.getUserId());
        jwtManager.compare(dto.getRefreshToken(),refreshToken);
        User user = userQuery.readById(dto.getUserId());
        String accessToken = jwtManager.generateAccessToken(JwtDto.of(user));
        return TokenDto.of(
                user.getId(),
                accessToken,
                refreshToken
        );
    }

    public TokenDto createTokenDto(User user){
        JwtDto jwtDto = JwtDto.of(user);
        String accessToken = jwtManager.generateAccessToken(jwtDto);
        String refreshToken = jwtManager.generateRefreshToken(jwtDto);
        return TokenDto.of(user.getId(), accessToken, refreshToken);
    }
}
