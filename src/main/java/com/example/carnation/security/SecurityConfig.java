package com.example.carnation.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationEntryPoint entryPoint;
    private final JwtSecurityFilter jwtSecurityFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // ✅ CORS 설정
                .csrf(AbstractHttpConfigurer::disable) // ✅ CSRF 비활성화
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // ✅ JWT 사용을 위한 STATELESS 설정
                .authorizeHttpRequests(auth -> {
                    // 공통 허용 경로 적용
                    SecurityWhitelistConfig.PERMIT_METHODS.forEach((method, urls) ->
                            urls.forEach(url -> auth.requestMatchers(method, url).permitAll())
                    );
                    // 그 외 요청은 인증 필요
                    auth.anyRequest().authenticated();
                })
                .addFilterBefore(jwtSecurityFilter, UsernamePasswordAuthenticationFilter.class) // ✅ JWT 필터 위치 조정
                .formLogin(AbstractHttpConfigurer::disable) // ✅ Form Login 비활성화
                .httpBasic(AbstractHttpConfigurer::disable) // ✅ HTTP Basic 인증 비활성화
                .logout(AbstractHttpConfigurer::disable) // ✅ Logout 비활성화
                .exceptionHandling(handler -> handler.authenticationEntryPoint(entryPoint)) // ✅ 인증 예외 처리
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        // ✅ 모든 Origin 허용 (보안 이슈 최소화를 위해 패턴 기반으로 설정)
        config.setAllowedOriginPatterns(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // ✅ 허용된 HTTP 메서드
        config.setAllowedHeaders(List.of("*")); // ✅ 모든 헤더 허용
        config.setAllowCredentials(true); // ✅ 인증 정보 허용
        config.setMaxAge(3600L); // ✅ Preflight 요청 캐싱 시간

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
