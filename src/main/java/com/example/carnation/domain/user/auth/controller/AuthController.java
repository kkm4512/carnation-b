package com.example.carnation.domain.user.auth.controller;

import com.example.carnation.common.response.ApiResponse;
import com.example.carnation.domain.user.auth.dto.*;
import com.example.carnation.domain.user.auth.service.AuthService;
import com.example.carnation.security.AuthUser;
import com.example.carnation.security.TokenDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.carnation.common.response.enums.BaseApiResponseEnum.SUCCESS;

@RestController
@RequestMapping("/api/v1/user/auth")
@RequiredArgsConstructor
@Tag(name = "Auth API", description = "사용자 인증 관련 API")
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "회원가입", description = "사용자가 회원가입을 요청합니다.")
    @PostMapping("/signup")
    public ApiResponse<Void> signup(@RequestBody @Valid SignupRequestDto dto) {
        authService.signUp(dto);
        return ApiResponse.of(SUCCESS);
    }

    @Operation(summary = "로그인", description = "사용자가 로그인하여 AccessToken, RefreshToken을 발급 받습니다.")
    @PostMapping("/signin")
    public ApiResponse<TokenDto> signin(@RequestBody @Valid SigninRequestDto dto) {
        TokenDto response = authService.signin(dto);
        return ApiResponse.of(SUCCESS, response);
    }

    /**
     * 로그아웃 (Refresh Token 삭제)
     */
    @Operation(summary = "로그아웃", description = "현재 사용자의 Refresh Token을 삭제합니다")
    @PostMapping("/signout")
    public ApiResponse<Void> signout(
            @AuthenticationPrincipal AuthUser authUser
    ) {
        authService.signout(authUser);
        return ApiResponse.of(SUCCESS);
    }

    @PostMapping("/send-code")
    @Operation(
            summary = "휴대폰 인증 코드 발송",
            description = """
            사용자의 휴대폰 번호로 인증 코드를 전송합니다. 
            4자리 인증 코드가 발송되며, 유효시간은 5분입니다. 
            
            ⚠️ 이미 인증 요청을 한 경우 기존 인증 코드가 덮어씌워집니다.
            """
    )
    public ApiResponse<Void> sendVerificationCode(@Valid @RequestBody PhoneAuthRequestDto dto) {
        authService.sendVerificationCode(dto);
        return ApiResponse.of(SUCCESS);
    }

    @PostMapping("/verify-code")
    @Operation(
            summary = "인증 코드 확인",
            description = """
            사용자가 입력한 인증 코드를 Redis에 저장된 값과 비교하여 인증을 처리합니다. 
            
            ✅ 인증 성공 시: API에서 `200 OK` 응답을 반환합니다.
            ❌ 인증 실패 시: 400, 404, 410 등의 에러 코드가 반환될 수 있습니다.
            
            🔹 인증 코드는 5분간 유효합니다.
            🔹 인증 코드 검증 후 자동으로 삭제됩니다.
            """
    )
    public ApiResponse<Void> verifyCode(@Valid @RequestBody PhoneAuthVerifyRequestDto dto) {
        authService.verifyCode(dto);
        return ApiResponse.of(SUCCESS);
    }

    /**
     * Refresh Token을 이용하여 Access Token 갱신
     */
    @Operation(summary = "토큰 갱신", description = "만료된 Access Token을 Refresh Token을 사용하여 새롭게 갱신합니다.")
    @PostMapping("/refresh")
    public ApiResponse<TokenDto> refreshAccessToken(@RequestBody @Valid TokenRefreshRequestDto dto) {
        TokenDto response = authService.refreshAccessToken(dto);
        return ApiResponse.of(SUCCESS, response);
    }
}
