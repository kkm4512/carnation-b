package com.example.carnation.domain.care.service;

import com.example.carnation.TestInfo;
import com.example.carnation.domain.care.cqrs.*;
import com.example.carnation.domain.care.dto.CareAssignmentResponse;
import com.example.carnation.domain.care.dto.CareHistoryRequestDto;
import com.example.carnation.domain.care.dto.CaregiverRequestDto;
import com.example.carnation.domain.care.dto.PatientRequestDto;
import com.example.carnation.domain.care.entity.CareHistory;
import com.example.carnation.domain.care.entity.CareMedia;
import com.example.carnation.domain.file.helper.FileHelper;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.example.carnation.TestInfo.*;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

@SpringBootTest
@ActiveProfiles("dev")
@Transactional
class CareHistoryServiceTest {

    // Service
    @Autowired
    CareHistoryService careHistoryService;
    @Autowired
    UserService userService;
    @Autowired
    CareAssignmentService careAssignmentService;
    @MockBean
    FileHelper fileHelper;

    // Command _ History
    @Autowired
    CareHistoryCommand careHistoryCommand;
    @Autowired
    CareHistoryQuery careHistoryQuery;
    @Autowired
    UserQuery userQuery;
    @Autowired
    CareAssignmentQuery careAssignmentQuery;
    @Autowired
    CaregiverQuery caregiverQuery;
    @Autowired
    PatientQuery patientQuery;
    // Object
    SignupRequestDto signupRequestDto1;

    @BeforeEach
    void init() {
        signupRequestDto1 = TestInfo.getSignupRequestDto1();
        userService.signUp(signupRequestDto1);
    }

    @Test
    @DisplayName("간병 기록 저장 및 연관관계 테스트")
    void test1() {
        // given - 피간병인과, 간병인의 정보가 주어짐
        User user = userQuery.findByEmail(signupRequestDto1.getEmail());
        AuthUser authUser = AuthUser.of(user);
        CaregiverRequestDto careGiverDto = getCaregiverRequestDto1();
        PatientRequestDto patientDto = getPatientRequestDto1();

        long beforeAssignmentCount = careAssignmentQuery.count();
        long beforeCareHistoryCount = careHistoryQuery.count();
        long beforeCaregiverCount = caregiverQuery.count();
        long beforePatientCount = patientQuery.count();

        // when - 간병 배정 생성
        CareAssignmentResponse careAssignment = careAssignmentService.create(authUser, careGiverDto, patientDto);


        // given - 간병 기록 입력 데이터
        String text = "환자 식사 제공 및 산책 수행";
        List<MultipartFile> imageFiles = getTestImages();
        List<MultipartFile> videoFiles = getTestVideos();
        CareHistoryRequestDto dto = new CareHistoryRequestDto(text, imageFiles, videoFiles);

        // when - 간병 기록 저장
        doNothing().when(fileHelper).uploads(any(), any()); // 실제 S3 업로드 행위 대체
        careHistoryService.create(authUser, careAssignment.getId(), dto);

        // then - CareAssignment이 정상적으로 저장되었는지 확인
        long afterAssignmentCount = careAssignmentQuery.count();
        assertEquals(beforeAssignmentCount + 1, afterAssignmentCount, "CareAssignment 개수가 증가해야 합니다.");

        // then - CareHistory가 정상적으로 저장되었는지 확인
        long afterCareHistoryCount = careHistoryQuery.count();
        assertEquals(beforeCareHistoryCount + 1, afterCareHistoryCount, "CareHistory 개수가 증가해야 합니다.");

        // then - Caregiver와 Patient가 정상적으로 생성되었는지 확인
        long afterCaregiverCount = caregiverQuery.count();
        long afterPatientCount = patientQuery.count();
        assertEquals(beforeCaregiverCount + 1, afterCaregiverCount, "Caregiver 개수가 증가해야 합니다.");
        assertEquals(beforePatientCount + 1, afterPatientCount, "Patient 개수가 증가해야 합니다.");

        // then - 방금 생성된 CareHistory 조회 및 연관 관계 확인
        CareHistory careHistory = careHistoryQuery.readAll().get((int) afterCareHistoryCount - 1);
        assertNotNull(careHistory, "CareHistory가 정상적으로 저장되어야 합니다.");
        assertEquals(text, careHistory.getText(), "CareHistory의 텍스트가 일치해야 합니다.");

        // then - CareHistory에 연결된 CareMedia(이미지, 비디오) 검증
        List<CareMedia> mediaList = careHistory.getMedias();
        assertNotNull(mediaList, "CareMedia 리스트가 null이면 안 됩니다.");
        assertEquals(imageFiles.size() + videoFiles.size(), mediaList.size(), "저장된 미디어 개수가 일치해야 합니다.");
    }


}