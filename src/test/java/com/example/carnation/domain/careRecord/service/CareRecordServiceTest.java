package com.example.carnation.domain.careRecord.service;

import com.example.carnation.domain.careRecord.dto.CaregiverRequestDto;
import com.example.carnation.domain.careRecord.dto.PatientRequestDto;
import com.example.carnation.domain.user.cqrs.UserQuery;
import com.example.carnation.domain.user.dto.SignupRequestDto;
import com.example.carnation.domain.user.entity.User;
import com.example.carnation.domain.user.service.UserService;
import com.example.carnation.security.AuthUser;
import com.example.carnation.testInfo.TestUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static com.example.carnation.testInfo.TestCareRecord.getCaregiverRequestDto1;
import static com.example.carnation.testInfo.TestCareRecord.getPatientRequestDto1;

@SpringBootTest
@ActiveProfiles("dev")
//@Transactional
class CareRecordServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    CareRecordService careRecordService;

    @Autowired
    UserQuery userQuery;

    SignupRequestDto signupRequestDto1;

    @BeforeEach
    void init() {
        signupRequestDto1 = TestUser.getSignupRequestDto1();
        userService.signUp(signupRequestDto1);
    }

    @Test
    @DisplayName(value = "간병 기록 생성시, 연관관계 생성 테스트")
    void test1(){
        // given - 피간병인과, 간병인의 정보가 주어짐
        User user = userQuery.findByEmail(signupRequestDto1.getEmail());
        AuthUser authUser = AuthUser.of(user);
        CaregiverRequestDto careGiverDto = getCaregiverRequestDto1();
        PatientRequestDto patientDto = getPatientRequestDto1();

        // when
        careRecordService.create(authUser,careGiverDto,patientDto);

        // then
    }

}