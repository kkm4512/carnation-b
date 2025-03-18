package com.example.carnation.domain.oAuth.controller.redirectController;

import com.example.carnation.common.response.ApiResponse;
import com.example.carnation.domain.oAuth.constans.OAuthProviderName;
import com.example.carnation.domain.oAuth.service.OAuthService;
import com.example.carnation.security.TokenDto;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.example.carnation.common.response.enums.BaseApiResponseEnum.SUCCESS;

@RestController
@RequestMapping("/api/v1/oAuth")
@RequiredArgsConstructor
@Hidden
@Tag(name = "OAuth API", description = "사용자 소셜 로그인 API") // 컨트롤러 설명
public class OAuthRedirectController {

    private final OAuthService oAuthService;

    @Operation(
            summary = "소셜 로그인 (카카오, 네이버, 구글)",
            description = "소셜 로그인 후 JWT 토큰을 반환합니다."
    )
    @GetMapping("/callback")
    public ApiResponse<TokenDto> socialLogin(
            @Parameter(
                    name = "provider",
                    description = "OAuth 제공자 (KAKAO, NAVER, GOOGLE)",
                    example = "KAKAO",
                    required = true,
                    in = ParameterIn.QUERY
            )
            @RequestParam OAuthProviderName oAuthProviderName,
            @Parameter(
                    name = "code",
                    description = "OAuth 인증 코드",
                    example = "4/P7q7W91a-oMsCeLvIaQm6bTrgtp7",
                    required = true,
                    in = ParameterIn.QUERY
            )
            @RequestParam("code") String code
    ) {
        TokenDto response = oAuthService.socialLogin(oAuthProviderName, code);
        return ApiResponse.of(SUCCESS,response);
    }
}

