package com.example.carnation.domain.user.controller;

import com.example.carnation.common.response.ApiResponse;
import com.example.carnation.domain.user.dto.SigninRequestDto;
import com.example.carnation.domain.user.dto.SignupRequestDto;
import com.example.carnation.domain.user.service.UserService;
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

import static com.example.carnation.common.response.enums.BaseApiResponse.SUCCESS;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User API", description = "사용자 회원가입 및 로그인 API") // 컨트롤러 전체에 대한 설명
public class UserController {
    private final UserService userService;

    @Operation(summary = "회원가입", description = "사용자가 회원가입을 요청합니다.")
    @PostMapping("/signup")
    public ApiResponse<Void> signup(@RequestBody @Valid SignupRequestDto dto) {
        userService.signUp(dto);
        return ApiResponse.of(SUCCESS);
    }

    @Operation(summary = "로그인", description = "사용자가 로그인하여 AccessToken, RefreshToken을 발급 받습니다.")
    @PostMapping("/signin")
    public ApiResponse<TokenDto> signin(@RequestBody @Valid SigninRequestDto dto) {
        TokenDto response = userService.signin(dto);
        return ApiResponse.of(SUCCESS, response);
    }

    /**
     * 로그아웃 (Refresh Token 삭제)
     */
    @Operation(summary = "로그아웃", description = "현재 사용자의 Refresh Token을 삭제하여 강제 로그아웃 처리합니다.")
    @PostMapping("/signout")
    public ApiResponse<Void> signout(@AuthenticationPrincipal AuthUser authUser) {
        userService.signout(authUser);
        return ApiResponse.of(SUCCESS);
    }
}
