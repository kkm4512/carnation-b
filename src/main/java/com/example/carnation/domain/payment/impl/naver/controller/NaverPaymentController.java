package com.example.carnation.domain.payment.impl.naver.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payment/naver")
@RequiredArgsConstructor
@Slf4j
public class NaverPaymentController {
    @GetMapping
    public String perpare() {
        return "perpare11";
    }
}
