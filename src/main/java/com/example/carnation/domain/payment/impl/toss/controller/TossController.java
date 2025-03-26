package com.example.carnation.domain.payment.impl.toss.controller;

import com.example.carnation.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.carnation.common.response.enums.BaseApiResponseEnum.SUCCESS;

@RestController
@RequestMapping("/api/v1/payment/toss")
@RequiredArgsConstructor
@Slf4j
@Hidden
public class TossController {
    @GetMapping("/health")
    public ApiResponse<Void> tossGetHealth() {
        return ApiResponse.of(SUCCESS);
    }

    @GetMapping
    public ApiResponse<String> findAllToss() {
        return ApiResponse.of(SUCCESS,"2");
    }
}
