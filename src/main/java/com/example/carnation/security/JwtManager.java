package com.example.carnation.security;

import com.example.carnation.common.exception.UserException;
import com.example.carnation.common.response.enums.UserApiResponseEnum;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

import static com.example.carnation.common.response.enums.UserApiResponseEnum.*;

@Slf4j(topic = "JwtManager")
@Component
public class JwtManager {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    private static final long ACCESS_TOKEN_EXPIRED_TIME = 30 * 60 * 1000L; // 30분
    private static final long REFRESH_TOKEN_EXPIRED_TIME = 60 * 60 * 1000L; // 1시간


    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private static final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public String generateAccessToken(final JwtDto jwtDto) {
        Date date = new Date();
        return BEARER_PREFIX + Jwts.builder()
                .setSubject(String.valueOf(jwtDto.getUserId()))
                .claim("email", jwtDto.getEmail())
                .claim("nickname", jwtDto.getNickname())
                .claim("userRole", jwtDto.getUserRole())
                .claim("authProvider", jwtDto.getAuthProvider())
                .setExpiration(new Date(date.getTime() + ACCESS_TOKEN_EXPIRED_TIME))
                .setIssuedAt(date) // 발급일
                .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                .compact();
    }

    public String generateRefreshToken(final JwtDto jwtDto) {
        Date date = new Date();
        return Jwts.builder()
                .setSubject(String.valueOf(jwtDto.getUserId()))
                .setExpiration(new Date(date.getTime() + REFRESH_TOKEN_EXPIRED_TIME)) // 만료 시간
                .setIssuedAt(date) // 발급일
                .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                .compact(); // 최종적인 jwt 생성
    }

    public Claims toClaims(String jwt) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();
        } catch (SecurityException | MalformedJwtException | IllegalArgumentException e) {
            throw new UserException(JWT_INVALID,e);
        } catch (ExpiredJwtException e) {
            throw new UserException(JWT_EXPIRED);
        } catch (UnsupportedJwtException e) {
            throw new UserException(JWT_UNSUPPORTED);
        } catch (io.jsonwebtoken.security.SignatureException e) {
            throw new UserException(JWT_NOT_SIGNATURED);
        }
    }

    public void validateRefreshToken(String refreshToken) {
        try {
            // 토큰을 Claims로 변환 (유효성 검증 포함)
            Jwts.parserBuilder()
                    .setSigningKey(key) // 서명 키 설정
                    .build()
                    .parseClaimsJws(refreshToken) // 토큰 파싱 및 검증
                    .getBody();
        } catch (SecurityException | MalformedJwtException | IllegalArgumentException e) {
            throw new UserException(JWT_INVALID, e);
        } catch (ExpiredJwtException e) {
            throw new UserException(JWT_EXPIRED, e);
        } catch (UnsupportedJwtException e) {
            throw new UserException(JWT_UNSUPPORTED, e);
        } catch (io.jsonwebtoken.security.SignatureException e) {
            throw new UserException(JWT_NOT_SIGNATURED, e);
        }
    }

    public void compare(String refreshToken, String refreshToken1) {
        if (refreshToken == null || !refreshToken.equals(refreshToken1)) {
            throw new UserException(UserApiResponseEnum.INVALID_REFRESH_TOKEN);
        }
    }
}