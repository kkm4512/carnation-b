package com.example.carnation.domain.payment.kakao.controller.redirectController;

import com.example.carnation.common.response.ApiResponse;
import com.example.carnation.domain.payment.interfaces.PaymentService;
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
    private final PaymentService paymentService;

    @GetMapping("/approval")
    public ApiResponse<Void> approval(
            @RequestParam("pg_token") String pgToken,
            @RequestParam("kakao_payment_ready_id") String kakaoPaymentReadyId
    ) {
        paymentService.approval(Long.valueOf(kakaoPaymentReadyId),pgToken);
        return ApiResponse.of(SUCCESS);
    }

    @GetMapping("/cancel")
    public void cancel() {
        log.info("cancel Callback");
    }
    @GetMapping("/fail")
    public void fail() {
        log.info("fail Callback");
    }
}
