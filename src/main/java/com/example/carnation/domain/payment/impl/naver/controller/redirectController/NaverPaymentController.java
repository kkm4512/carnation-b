package com.example.carnation.domain.payment.impl.naver.controller.redirectController;

import com.example.carnation.common.response.ApiResponse;
import com.example.carnation.domain.payment.impl.kakao.constans.PaymentStatus;
import com.example.carnation.domain.payment.impl.naver.service.NaverPaymentService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.example.carnation.common.response.enums.BaseApiResponseEnum.SUCCESS;

@RestController
@RequestMapping("/api/v1/payment/naver/callback")
@RequiredArgsConstructor
@Slf4j
@Hidden
public class NaverPaymentController {
    private final NaverPaymentService naverPaymentService;

    @GetMapping("/approval")
    public ApiResponse<PaymentStatus> approval(
            @RequestParam("order_id") String orderId
    ) {
        String pgToken = null;
        PaymentStatus response = naverPaymentService.approval(Long.valueOf(orderId), pgToken);
        return ApiResponse.of(SUCCESS,response);
    }

}
