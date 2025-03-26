package com.example.carnation.domain.user.oAuth.dto;

import com.example.carnation.common.exception.OAuthException;
import com.example.carnation.domain.user.oAuth.constans.OAuthProviderNameType;
import com.example.carnation.init.PropertyInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.example.carnation.common.response.enums.OAuthApiResponseEnum.OAUTH_NOT_FOUND_PROVIDER;
import static com.example.carnation.domain.user.oAuth.constans.OAuthProviderNameType.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Schema(description = "OAuth 제공자 DTO")
public class OAuthProviderDto {

    @Schema(description = "OAuth 제공자 이름", example = "kakao")
    private OAuthProviderNameType nameEnum;

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

    public OAuthProviderDto(OAuthProviderNameType nameEnum, String tokenUrl, String userInfoUrl, String redirectUrl, String code) {
        this.nameEnum = nameEnum;
        this.tokenUrl = tokenUrl;
        this.userInfoUrl = userInfoUrl;
        this.redirectUrl = redirectUrl;
        this.code = code;
    }

    public static OAuthProviderDto of(OAuthProviderNameType nameType, String code) {
        return switch (nameType) {
            case KAKAO -> new OAuthProviderDto(KAKAO,
                    "https://kauth.kakao.com/oauth/token",
                    "https://kapi.kakao.com/v2/user/me",
                    PropertyInfo.SOCIAL_REDIRECT_URL  + "?oAuthProviderName=" + nameType.name(),
                    code
            );

            case NAVER -> new OAuthProviderDto(NAVER,
                    "https://nid.naver.com/oauth2.0/token",
                    "https://openapi.naver.com/v1/nid/me",
                    PropertyInfo.SOCIAL_REDIRECT_URL + "?oAuthProviderName=" + nameType.name(),
                    code
            );

            case GOOGLE -> new OAuthProviderDto(GOOGLE,
                    "https://oauth2.googleapis.com/token",
                    "https://www.googleapis.com/oauth2/v3/userinfo",
                    PropertyInfo.SOCIAL_REDIRECT_URL + "?oAuthProviderName=" + nameType.name(),
                    code
            );

            default -> throw new OAuthException(OAUTH_NOT_FOUND_PROVIDER);
        };
    }

    public void updateAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

}
