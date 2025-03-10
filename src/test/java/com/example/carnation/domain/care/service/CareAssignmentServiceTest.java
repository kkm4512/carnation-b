package com.example.carnation.domain.care.service;

import com.example.carnation.TestInfo;
import com.example.carnation.domain.care.cqrs.CareAssignmentQuery;
import com.example.carnation.domain.care.cqrs.CaregiverQuery;
import com.example.carnation.domain.care.cqrs.PatientQuery;
import com.example.carnation.domain.care.dto.CaregiverRequestDto;
import com.example.carnation.domain.care.dto.PatientRequestDto;
import com.example.carnation.domain.care.entity.CareAssignment;
import com.example.carnation.domain.user.cqrs.UserQuery;
import com.example.carnation.domain.user.dto.SignupRequestDto;
import com.example.carnation.domain.user.entity.User;
import com.example.carnation.domain.user.service.UserService;
import com.example.carnation.security.AuthUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static com.example.carnation.TestInfo.getCaregiverRequestDto1;
import static com.example.carnation.TestInfo.getPatientRequestDto1;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("dev")
@Transactional
class CareAssignmentServiceTest {

    // Service
    @Autowired
    private UserService userService;
    @Autowired
    CareAssignmentService careAssignmentService;

    // Query _ Command
    @Autowired
    UserQuery userQuery;
    @Autowired
    CaregiverQuery caregiverQuery;
    @Autowired
    PatientQuery patientQuery;
    @Autowired
    CareAssignmentQuery careAssignmentQuery;

    // Object
    SignupRequestDto signupRequestDto1;

    @BeforeEach
    void init() {
        signupRequestDto1 = TestInfo.getSignupRequestDto1();
        userService.signUp(signupRequestDto1);
    }

    @Test
    @DisplayName(value = "간병 배정 생성시, 연관관계 생성 테스트")
    void test1(){
        // given - 피간병인과, 간병인의 정보가 주어짐
        User user = userQuery.findByEmail(signupRequestDto1.getEmail());
        AuthUser authUser = AuthUser.of(user);
        CaregiverRequestDto careGiverDto = getCaregiverRequestDto1();
        PatientRequestDto patientDto = getPatientRequestDto1();

        long beforeCareAssignmentCount = careAssignmentQuery.count();
        long beforeCaregiverCount = caregiverQuery.count();
        long beforePatientCount = patientQuery.count();

        // when - 간병 기록 생성
        CareAssignment careAssignment = careAssignmentService.create(authUser, careGiverDto, patientDto);

        // then - CareRecord가 정상적으로 저장되었는지 검증
        long afterCareAssignmentCount = careAssignmentQuery.count();
        long afterCaregiverCount = caregiverQuery.count();
        long afterPatientCount = patientQuery.count();

        // 1. CareRecord가 정상적으로 저장되었는지 확인
        assertEquals(beforeCareAssignmentCount + 1, afterCareAssignmentCount, "CareAssignment 개수가 증가해야 합니다.");

        // 2. Caregiver와 Patient가 정상적으로 저장되었는지 확인
        assertEquals(beforeCaregiverCount + 1, afterCaregiverCount, "Caregiver 개수가 증가해야 합니다.");
        assertEquals(beforePatientCount + 1, afterPatientCount, "Patient 개수가 증가해야 합니다.");


        // 4. CareRecord와 Caregiver, Patient의 연관관계 검증
        assertNotNull(careAssignment, "careAssignment가 정상적으로 저장되어야 합니다.");
        assertNotNull(careAssignment.getCaregiver(), "Caregiver가 careAssignment와 연관되어야 합니다.");
        assertNotNull(careAssignment.getPatient(), "Patient가 careAssignment와 연관되어야 합니다.");

        // 5. Caregiver와 Patient의 정보가 정상적으로 저장되었는지 확인
        assertEquals(careGiverDto.getName(), careAssignment.getCaregiver().getName(), "Caregiver 이름이 일치해야 합니다.");
        assertEquals(patientDto.getName(), careAssignment.getPatient().getName(), "Patient 이름이 일치해야 합니다.");
    }

}