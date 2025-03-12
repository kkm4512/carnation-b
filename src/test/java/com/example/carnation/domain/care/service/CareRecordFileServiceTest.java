package com.example.carnation.domain.care.service;

import com.example.carnation.TestInfo;
import com.example.carnation.domain.care.dto.CareAssignmentResponse;
import com.example.carnation.domain.care.dto.CaregiverRequestDto;
import com.example.carnation.domain.care.dto.PatientRequestDto;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("dev")
@Transactional
class CareRecordFileServiceTest {

    // Service
    @Autowired
    UserService userService;
    @Autowired
    CareAssignmentService careAssignmentService;
    @Autowired
    CareRecordFileService careRecordFileService;

    // Command _ History
    @Autowired
    UserQuery userQuery;

    // Object
    SignupRequestDto signupRequestDto1;

    @BeforeEach
    void init() {
        signupRequestDto1 = TestInfo.getSignupRequestDto1();
        userService.signUp(signupRequestDto1);
    }
    @Test
    @DisplayName("간병 배정 PDF 파일 정상 생성 테스트")
    void testGeneratePdf() {
        // given - 피간병인과, 간병인의 정보가 주어짐
        User user = userQuery.findByEmail(signupRequestDto1.getEmail());
        AuthUser authUser = AuthUser.of(user);
        CaregiverRequestDto careGiverDto = getCaregiverRequestDto1();
        PatientRequestDto patientDto = getPatientRequestDto1();

        // when - 간병 배정 생성
        CareAssignmentResponse careAssignment = careAssignmentService.create(authUser, careGiverDto, patientDto);

        // when - pdf 파일 생성 요청
        byte[] pdfBytes = careRecordFileService.generateAssignmentPdf(authUser, careAssignment.getId());

        // then - 응답이 정상적으로 반환되는지 검증
        assertNotNull(pdfBytes, "🚨 PDF 바이트 데이터가 null이면 안됩니다.");
        assertTrue(pdfBytes.length > 0, "🚨 PDF 바이트 데이터가 비어 있으면 안됩니다.");
    }


}