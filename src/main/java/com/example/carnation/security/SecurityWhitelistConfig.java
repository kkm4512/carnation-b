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
                    "/api/v1/users/**"
            ),
            HttpMethod.PUT, List.of(
                    "/api/v1/users/**"
            ),
            HttpMethod.GET, List.of(
                    "/health"
            ),
            HttpMethod.OPTIONS, List.of("/**") // Preflight 요청 허용
    );

}
