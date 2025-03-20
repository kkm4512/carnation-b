package com.example.carnation.domain.payment.kakao.controller.redirectController;

import com.example.carnation.common.response.ApiResponse;
import com.example.carnation.domain.payment.kakao.constans.KakaoPaymentStatus;
import com.example.carnation.domain.payment.kakao.service.KakaoPaymentService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.example.carnation.common.response.enums.BaseApiResponseEnum.SUCCESS;

@RestController
@RequestMapping("/api/v1/payment/kakao/callback")
@RequiredArgsConstructor
@Slf4j
@Hidden
public class KakaoPaymentRedirectController {
    private final KakaoPaymentService kakaoPaymentService;

    @GetMapping("/approval")
    public ApiResponse<KakaoPaymentStatus> approval(
            @RequestParam("pg_token") String pgToken,
            @RequestParam("kakao_payment_id") String kakaoPaymentReadyId
    ) {
        KakaoPaymentStatus response = kakaoPaymentService.approval(Long.valueOf(kakaoPaymentReadyId), pgToken);
        return ApiResponse.of(SUCCESS,response);
    }

    @GetMapping("/cancel")
    public ApiResponse<KakaoPaymentStatus> cancel(@RequestParam("kakao_payment_id") String kakaoPaymentReadyId) {
        KakaoPaymentStatus response = kakaoPaymentService.cancel(Long.valueOf(kakaoPaymentReadyId));
        return ApiResponse.of(SUCCESS,response);
    }
    @GetMapping("/fail")
    public ApiResponse<KakaoPaymentStatus> fail(@RequestParam("kakao_payment_id") String kakaoPaymentReadyId) {
        KakaoPaymentStatus response = kakaoPaymentService.fail(Long.valueOf(kakaoPaymentReadyId));
        return ApiResponse.of(SUCCESS,response);
    }
}
