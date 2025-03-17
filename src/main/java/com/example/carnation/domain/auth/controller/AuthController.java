package com.example.carnation.domain.auth.controller;

import com.example.carnation.common.response.ApiResponse;
import com.example.carnation.domain.auth.dto.PhoneAuthRequestDto;
import com.example.carnation.domain.auth.dto.PhoneAuthVerifyRequestDto;
import com.example.carnation.domain.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.carnation.common.response.enums.BaseApiResponseEnum.SUCCESS;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Auth API", description = "휴대폰 인증 관련 API")
public class AuthController {
    private final AuthService authService;

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
}
