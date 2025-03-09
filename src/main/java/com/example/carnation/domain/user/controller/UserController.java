package com.example.carnation.domain.user.controller;

import com.example.carnation.common.response.ApiResponse;
import com.example.carnation.common.response.enums.BaseApiResponse;
import com.example.carnation.domain.user.dto.req.*;
import com.example.carnation.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ApiResponse<Void> signup(@RequestBody @Valid SignupRequestDto dto){
        userService.signUp(dto);
        return ApiResponse.of(BaseApiResponse.SUCCESS);
    }

    @PostMapping("/signin")
    public ApiResponse<String> signin(@RequestBody @Valid SigninRequestDto dto){
        String result = userService.signin(dto);
        return ApiResponse.of(BaseApiResponse.SUCCESS, result);
    }
}
