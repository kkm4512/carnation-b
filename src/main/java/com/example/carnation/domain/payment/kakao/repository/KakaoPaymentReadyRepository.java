package com.example.carnation.domain.payment.kakao.repository;

import com.example.carnation.domain.payment.kakao.entity.KakaoPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KakaoPaymentReadyRepository extends JpaRepository<KakaoPayment, Long> {
}
