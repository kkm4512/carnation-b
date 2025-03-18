package com.example.carnation.domain.payment.kakao.repository;

import com.example.carnation.domain.payment.kakao.entity.KakaoPaymentReady;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KakaoPaymentReadyRepository extends JpaRepository<KakaoPaymentReady, Long> {
}
