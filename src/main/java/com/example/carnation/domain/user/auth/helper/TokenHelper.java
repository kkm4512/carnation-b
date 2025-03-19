package com.example.carnation.domain.user.auth.helper;

import com.example.carnation.domain.user.common.entity.User;
import com.example.carnation.security.JwtDto;
import com.example.carnation.security.JwtManager;
import com.example.carnation.security.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenHelper {
    private final JwtManager jwtManager;

    public TokenDto createTokenDto(User user){
        JwtDto jwtDto = JwtDto.of(user);
        String accessToken = jwtManager.generateAccessToken(jwtDto);
        String refreshToken = jwtManager.generateRefreshToken(jwtDto);
        return TokenDto.of(user.getId(), accessToken, refreshToken);
    }

}
