package com.example.carnation.domain.oAuth.controller;

import com.example.carnation.common.exception.OAuthException;
import com.example.carnation.common.response.ApiResponse;
import com.example.carnation.domain.oAuth.constans.OAuthProviderName;
import com.example.carnation.init.StaticProperties;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.example.carnation.common.response.enums.BaseApiResponseEnum.SUCCESS;
import static com.example.carnation.common.response.enums.OAuthApiResponseEnum.OAUTH_NOT_FOUND_PROVIDER;

@RestController
@Tag(name = "OAuth Login Redirect URL API")
public class OAuthController {

    private static final String KAKAO_AUTH_URL = "https://kauth.kakao.com/oauth/authorize";
    private static final String NAVER_AUTH_URL = "https://nid.naver.com/oauth2.0/authorize";
    private static final String GOOGLE_AUTH_URL = "https://accounts.google.com/o/oauth2/auth";
    @Operation(
            summary = "OAuth 로그인 페이지 리다이렉트 URL 생성",
            description = "카카오, 네이버, 구글 로그인 페이지의 URL을 생성하여 반환합니다."
    )
    @GetMapping("/api/v1/social-login-url")
    public ApiResponse<String> getSocialLoginURL(
            @RequestParam("name") OAuthProviderName oAuthProviderName
    ) {
        try {
            final String REDIRECT_URI = StaticProperties.getRedirectUrl();
            String clientId = getClientId(oAuthProviderName); // ✅ 동적으로 클라이언트 ID 가져오기
            String url = switch (oAuthProviderName) {
                case KAKAO -> KAKAO_AUTH_URL
                        + "?response_type=code"
                        + "&client_id=" + clientId
                        + "&redirect_uri=" + REDIRECT_URI + "?oAuthProviderName=" + oAuthProviderName;

                case NAVER -> NAVER_AUTH_URL
                        + "?response_type=code"
                        + "&client_id=" + clientId
                        + "&redirect_uri=" + REDIRECT_URI + "?oAuthProviderName=" + oAuthProviderName;

                case GOOGLE -> GOOGLE_AUTH_URL
                        + "?response_type=code"
                        + "&client_id=" + clientId
                        + "&redirect_uri=" + REDIRECT_URI + "?oAuthProviderName=" + oAuthProviderName
                        + "&scope=email%20profile";

                default -> throw new OAuthException(OAUTH_NOT_FOUND_PROVIDER);
            };
            return ApiResponse.of(SUCCESS,url) ;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    // ✅ OAuth 제공자별 클라이언트 ID 동적 설정
    private String getClientId(OAuthProviderName provider) {
        return switch (provider) {
            case KAKAO -> StaticProperties.getKakaoClientId();
            case NAVER -> StaticProperties.getNaverClientId();
            case GOOGLE -> StaticProperties.getGoogleClientId();
            default -> throw new OAuthException(OAUTH_NOT_FOUND_PROVIDER);
        };
    }
}
