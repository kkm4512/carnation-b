package com.example.carnation.domain.payment.interfaces;

import com.example.carnation.domain.payment.kakao.dto.KakaoPaymentApprovalResponseDto;
import com.example.carnation.domain.payment.kakao.dto.KakaoPaymentReadyRequestDto;
import com.example.carnation.domain.payment.kakao.dto.KakaoPaymentReadyResponseDto;
import com.example.carnation.security.AuthUser;

public interface PaymentService {
    KakaoPaymentReadyResponseDto ready(AuthUser authUser, KakaoPaymentReadyRequestDto dto);
    KakaoPaymentApprovalResponseDto approval(Long kakaoPaymentReadyId,String pgToken);
}
