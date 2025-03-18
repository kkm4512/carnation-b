package com.example.carnation.domain.payment.kakao.controller;

import com.example.carnation.common.response.ApiResponse;
import com.example.carnation.domain.payment.kakao.dto.KakaoPaymentReadyRequestDto;
import com.example.carnation.domain.payment.kakao.dto.KakaoPaymentReadyResponseDto;
import com.example.carnation.domain.payment.interfaces.PaymentService;
import com.example.carnation.security.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.example.carnation.common.response.enums.BaseApiResponseEnum.SUCCESS;

@RestController
@RequestMapping("/api/v1/payment/kakao")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Payment API", description = "카카오페이 결제 관련 API")
public class KakaoPaymentController {
    private final PaymentService paymentService;

    @Operation(summary = "카카오페이 결제 준비", description = "사용자가 결제 준비 요청을 보내면, 카카오페이 결제 프로세스를 시작합니다.")
    @SecurityRequirement(name = "JWT")
    @PostMapping(value = "/ready")
    public ApiResponse<KakaoPaymentReadyResponseDto> ready(
            @AuthenticationPrincipal AuthUser authUser,
            @Valid @RequestBody KakaoPaymentReadyRequestDto dto
    ) {
        KakaoPaymentReadyResponseDto response = paymentService.ready(authUser, dto);
        return ApiResponse.of(SUCCESS,response);
    }
}
