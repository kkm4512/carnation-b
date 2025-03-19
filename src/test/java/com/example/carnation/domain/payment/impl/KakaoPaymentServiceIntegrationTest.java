package com.example.carnation.domain.payment.impl;

import com.example.carnation.domain.payment.MockPaymentInfo;
import com.example.carnation.domain.payment.kakao.dto.KakaoPaymentReadyRequestDto;
import com.example.carnation.domain.payment.kakao.dto.KakaoPaymentReadyResponseDto;
import com.example.carnation.domain.payment.kakao.service.KakaoPaymentService;
import com.example.carnation.domain.user.MockUserTestInfo;
import com.example.carnation.domain.user.common.cqrs.UserCommand;
import com.example.carnation.domain.user.common.entity.User;
import com.example.carnation.security.AuthUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest // ✅ 실제 빈을 로드하여 통합 테스트
@ActiveProfiles(profiles = "dev") // ✅ application-dev.yml을 사용
@Transactional
public class KakaoPaymentServiceIntegrationTest {

    @Autowired
    KakaoPaymentService kakaoPaymentService; // ✅ 실제 서비스 빈 주입

    @Autowired
    UserCommand userCommand;

    @Test
    @DisplayName("카카오 페이먼트 결제 준비 성공 테스트")
    public void test1() {
        // given
        User user1 = MockUserTestInfo.getUserNoId1(); // ✅ User 객체 생성 (비영속 상태)
        userCommand.create(user1); // ✅ 영속 상태로 변환되는 시점
        KakaoPaymentReadyRequestDto dto = MockPaymentInfo.getKakaoPaymentReadyRequestDto1();
        AuthUser authUser = MockUserTestInfo.getAuthUser1();

        // when
        KakaoPaymentReadyResponseDto response = kakaoPaymentService.ready(authUser,dto);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getTid()).isNotEmpty(); // ✅ TID 값이 비어있지 않은지 확인
        assertThat(response.getNextRedirectPcUrl()).isNotEmpty(); // ✅ PC Redirect URL 확인
        assertThat(response.getNextRedirectMobileUrl()).isNotEmpty(); // ✅ Mobile Redirect URL 확인
        assertThat(response.getNextRedirectAppUrl()).isNotEmpty(); // ✅ App Redirect URL 확인
    }

    @Test
    @DisplayName("잘못된 요청으로 결제 준비 실패 테스트")
    public void test2() {
        // given - 필수 필드 누락 (totalAmount 누락)
        KakaoPaymentReadyRequestDto dto = MockPaymentInfo.getKakaoPaymentReadyRequestRequiredDtoFail1();
        AuthUser authUser = MockUserTestInfo.getAuthUser1();

        // when & then - 예외 발생 검증
        assertThrows(RuntimeException.class, () -> kakaoPaymentService.ready(authUser,dto));
    }

}
