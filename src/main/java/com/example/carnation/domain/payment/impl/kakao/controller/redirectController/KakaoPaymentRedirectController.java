package com.example.carnation.domain.payment.impl.kakao.controller.redirectController;

import com.example.carnation.common.response.ApiResponse;
import com.example.carnation.domain.payment.impl.kakao.constans.KakaoPaymentStatus;
import com.example.carnation.domain.payment.impl.kakao.service.KakaoPaymentService;
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
            @RequestParam("order_id") String orderId
    ) {
        KakaoPaymentStatus response = kakaoPaymentService.approval(Long.valueOf(orderId), pgToken);
        return ApiResponse.of(SUCCESS,response);
    }

    @GetMapping("/cancel")
    public ApiResponse<KakaoPaymentStatus> cancel(@RequestParam("order_id") String orderId) {
        KakaoPaymentStatus response = kakaoPaymentService.cancel(Long.valueOf(orderId));
        return ApiResponse.of(SUCCESS,response);
    }
    @GetMapping("/fail")
    public ApiResponse<KakaoPaymentStatus> fail(@RequestParam("order_id") String orderId) {
        KakaoPaymentStatus response = kakaoPaymentService.fail(Long.valueOf(orderId));
        return ApiResponse.of(SUCCESS,response);
    }
}
