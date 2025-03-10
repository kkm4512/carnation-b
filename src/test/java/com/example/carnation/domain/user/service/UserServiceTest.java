package com.example.carnation.domain.user.service;

import com.example.carnation.domain.user.constans.UserType;
import com.example.carnation.domain.user.dto.SigninRequestDto;
import com.example.carnation.domain.user.dto.SignupRequestDto;
import com.example.carnation.domain.user.entity.User;
import com.example.carnation.domain.user.repository.UserRepository;
import com.example.carnation.security.UserRole;
import com.example.carnation.testInfo.TestUser;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

import static com.example.carnation.testInfo.TestUser.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("dev")
@Transactional
class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    SignupRequestDto signupRequestDto1;

    @BeforeEach
    void init() {
        signupRequestDto1 = getSignupRequestDto1();
    }

    @Test
    @DisplayName("유저_회원가입_테스트")
    void test1() {
        // given - BeforeEach로 기존 사용자의 정보 저장

        // when - 회원가입 시도
        userService.signUp(signupRequestDto1);

        // then - DB조회시, 정상적으로 있는지 확인
        Optional<User> findUser = userRepository.findByEmail("test@naver.com1");
        assertThat(findUser).isPresent();
        assertEquals(UserRole.ROLE_USER, findUser.get().getUserRole());
        assertEquals(UserType.CAREGIVER, findUser.get().getUserType());

    }

    @Test
    @DisplayName("유저_로그인_테스트")
    void test2() {
        // given - 로그인 할 사용자의 정보 준비
        SigninRequestDto signinRequestDto1 = TestUser.getSigninRequestDto1();

        // when - 회원가입
        userService.signUp(signupRequestDto1);

        // when & then - 로그인 시도, 예외 미발생시 테스트 성공
        assertDoesNotThrow(() -> userService.signin(signinRequestDto1));
    }
}