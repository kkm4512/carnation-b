package com.example.carnation.domain.oAuth.dto;

import com.example.carnation.common.exception.OAuthException;
import com.example.carnation.domain.oAuth.constans.OAuthProviderName;
import com.example.carnation.init.OAuthProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.example.carnation.common.response.enums.OAuthApiResponse.OAUTH_NOT_FOUND_PROVIDER;
import static com.example.carnation.domain.oAuth.constans.OAuthProviderName.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Schema(description = "OAuth 제공자 DTO")
public class OAuthProviderDto {

    @Schema(description = "OAuth 제공자 이름", example = "kakao")
    private OAuthProviderName oAuthProviderName;

    @Schema(description = "OAuth 토큰 발급 URL", example = "https://kauth.kakao.com/oauth/token")
    private String tokenUrl;

    @Schema(description = "OAuth 사용자 정보 요청 URL", example = "https://kapi.kakao.com/v2/user/me")
    private String userInfoUrl;

    @Schema(description = "oAuth에 등록한 redirectURL")
    private String redirectUrl;

    @Schema(description = "oAuth 요청시 필요한 code")
    private String code;

    @Schema(description = "oAuth 요청후 받은 토큰")
    private String accessToken;

    public OAuthProviderDto(OAuthProviderName oAuthProviderName, String tokenUrl, String userInfoUrl, String redirectUrl, String code) {
        this.oAuthProviderName = oAuthProviderName;
        this.tokenUrl = tokenUrl;
        this.userInfoUrl = userInfoUrl;
        this.redirectUrl = redirectUrl;
        this.code = code;
    }

    public static OAuthProviderDto of(OAuthProviderName oAuthProviderName, String code) {
        return switch (oAuthProviderName) {
            case KAKAO -> new OAuthProviderDto(KAKAO,
                    "https://kauth.kakao.com/oauth/token",
                    "https://kapi.kakao.com/v2/user/me",
                    OAuthProperties.getRedirectUrl() + "?oAuthProviderName=" + oAuthProviderName.name(),
                    code
            );

            case NAVER -> new OAuthProviderDto(NAVER,
                    "https://nid.naver.com/oauth2.0/token",
                    "https://openapi.naver.com/v1/nid/me",
                    OAuthProperties.getRedirectUrl() + "?=" + oAuthProviderName.name(),
                    code
            );

            case GOOGLE -> new OAuthProviderDto(GOOGLE,
                    "https://oauth2.googleapis.com/token",
                    "https://www.googleapis.com/oauth2/v3/userinfo",
                    OAuthProperties.getRedirectUrl() + "?=" + oAuthProviderName.name(),
                    code
            );

            default -> throw new OAuthException(OAUTH_NOT_FOUND_PROVIDER);
        };
    }

    public void updateAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

}
