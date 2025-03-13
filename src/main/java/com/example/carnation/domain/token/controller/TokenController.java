package com.example.carnation.domain.token.controller;

import com.example.carnation.common.response.ApiResponse;
import com.example.carnation.domain.token.dto.TokenRefreshRequestDto;
import com.example.carnation.domain.token.service.TokenService;
import com.example.carnation.security.TokenDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.carnation.common.response.enums.BaseApiResponse.SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/token")
@Tag(name = "Token API", description = "토큰 관련 API")
public class TokenController {

    private final TokenService authService;

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
