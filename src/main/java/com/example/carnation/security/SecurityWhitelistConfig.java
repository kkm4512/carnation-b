package com.example.carnation.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import java.util.List;
import java.util.Map;

@Configuration
public class SecurityWhitelistConfig {

    // 특정 HTTP 메서드별 허용 경로
    public static final Map<HttpMethod, List<String>> PERMIT_METHODS = Map.of(
            HttpMethod.POST, List.of(
                    "/api/v1/users/signup",
                    "/api/v1/users/signin",
                    "/api/v1/token/**"
            ),
            HttpMethod.PUT, List.of(
            ),
            HttpMethod.GET, List.of(
                    "/health",
                    "/swagger-ui/**",
                    "/api-docs/**",
                    "/docs",
                    "/api/v1/oAuth/**",
                    "/favicon.ico",
                    "/socialLoginURL"
            ),
            HttpMethod.OPTIONS, List.of("/**") // Preflight 요청 허용
    );

}
