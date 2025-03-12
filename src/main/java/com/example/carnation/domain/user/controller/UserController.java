package com.example.carnation.domain.user.controller;

import com.example.carnation.common.response.ApiResponse;
import com.example.carnation.common.response.enums.BaseApiResponse;
import com.example.carnation.domain.user.dto.SigninRequestDto;
import com.example.carnation.domain.user.dto.SignupRequestDto;
import com.example.carnation.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
        return ApiResponse.of(BaseApiResponse.SUCCESS);
    }

    @Operation(summary = "로그인", description = "사용자가 로그인하여 JWT 토큰을 발급받습니다.")
    @PostMapping("/signin")
    public ApiResponse<String> signin(@RequestBody @Valid SigninRequestDto dto) {
        String result = userService.signin(dto);
        return ApiResponse.of(BaseApiResponse.SUCCESS, result);
    }
}
