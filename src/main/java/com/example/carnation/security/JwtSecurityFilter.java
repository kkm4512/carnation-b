package com.example.carnation.security;

import com.example.carnation.common.exception.UserException;
import com.example.carnation.domain.user.constans.AuthProvider;
import com.example.carnation.domain.user.constans.UserType;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.example.carnation.common.response.enums.UserApiResponse.NOT_LOGIN;
import static com.example.carnation.security.JwtManager.AUTHORIZATION_HEADER;
import static com.example.carnation.security.JwtManager.BEARER_PREFIX;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtSecurityFilter extends OncePerRequestFilter {

    private final JwtManager jm;
    private static final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        HttpMethod method = HttpMethod.valueOf(request.getMethod());

        // 특정 Set<String>에 포함된 경로면 필터 제외 (빠른 조회)
        if (SecurityWhitelistConfig.PERMIT_METHODS.containsKey(method)) {
            for (String urlPattern : SecurityWhitelistConfig.PERMIT_METHODS.get(method)) {
                if (pathMatcher.match(urlPattern, requestURI)) {
                    return true; // 필터 비활성화
                }
            }
        }

        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpRequest, @NonNull HttpServletResponse httpResponse, @NonNull FilterChain chain) throws ServletException, IOException {
        String jwt = httpRequest.getHeader(AUTHORIZATION_HEADER);

        try {
            if (jwt != null) {
                jwt = jwt.replace(BEARER_PREFIX, "");
                Claims claims = jm.toClaims(jwt);
                Long userId = Long.parseLong(claims.getSubject());
                String email = claims.get("email", String.class);
                String nickname = claims.get("nickname", String.class);
                String userRoleString = claims.get("userRole", String.class);
                String userTypeString = claims.get("userType", String.class);
                String authProviderString = claims.get("authProvider", String.class);
                UserRole userRole = UserRole.valueOf(userRoleString);
                UserType userType = UserType.valueOf(userTypeString);
                AuthProvider authProvider = AuthProvider.valueOf(authProviderString);

                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    AuthUser authUser = new AuthUser(userId, email, nickname, userRole,userType,authProvider);
                    JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(authUser);
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
            else {
                throw new UserException(NOT_LOGIN);
            }
            chain.doFilter(httpRequest, httpResponse);
        } catch (Exception e) {
            httpRequest.setAttribute("exception", e);
            throw e; // EntryPoint로 전달
        }
    }
}
