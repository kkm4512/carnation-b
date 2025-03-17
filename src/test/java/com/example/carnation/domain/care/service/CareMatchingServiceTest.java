package com.example.carnation.domain.care.service;

import com.example.carnation.domain.care.CareMockTestInfo;
import com.example.carnation.domain.care.cqrs.CareMatchingCommand;
import com.example.carnation.domain.care.cqrs.CareMatchingQuery;
import com.example.carnation.domain.care.cqrs.CaregiverQuery;
import com.example.carnation.domain.care.cqrs.PatientQuery;
import com.example.carnation.domain.care.dto.CareMatchingRequestDto;
import com.example.carnation.domain.care.dto.CareMatchingResponse;
import com.example.carnation.domain.care.entity.CareMatching;
import com.example.carnation.domain.care.entity.Caregiver;
import com.example.carnation.domain.care.entity.Patient;
import com.example.carnation.domain.user.cqrs.UserQuery;
import com.example.carnation.domain.user.entity.User;
import com.example.carnation.security.AuthUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class CareMatchingServiceTest {

    @InjectMocks
    private CareMatchingService careMatchingService; // 테스트 대상

    @Mock
    private CareMatchingQuery careMatchingQuery;

    @Mock
    private CareMatchingCommand careMatchingCommand;

    @Mock
    private CaregiverQuery caregiverQuery;

    @Mock
    private PatientQuery patientQuery;

    @Mock
    private UserQuery userQuery;

    private AuthUser authUser1;
    private AuthUser authUser2;
    private User user1;
    private User user2;
    private Caregiver caregiver1;
    private Caregiver caregiver2;
    private Patient patient1;
    private Patient patient2;
    private CareMatchingRequestDto requestDtoPatient1;
    private CareMatchingRequestDto requestDtoPatient2;
    private CareMatchingRequestDto requestDtoCaregiver1;
    private CareMatchingRequestDto requestDtoCaregiver2;
    private CareMatching careMatching1;
    private CareMatching careMatching2;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        authUser1 = CareMockTestInfo.getAuthUser1();
        authUser2 = CareMockTestInfo.getAuthUser2();
        user1 = CareMockTestInfo.getUser1();
        user2 = CareMockTestInfo.getUser2();
        caregiver1 = CareMockTestInfo.getCaregiver1();
        patient1 = CareMockTestInfo.getPatient1();
        caregiver2 = CareMockTestInfo.getCaregiver2();
        patient2 = CareMockTestInfo.getPatient2();

        pageable = PageRequest.of(0, 10, Sort.by("id").ascending());

        requestDtoPatient1 = CareMockTestInfo.getCareMatchingRequestDtoForPatient1();
        requestDtoPatient2 = CareMockTestInfo.getCareMatchingRequestDtoForPatient2();
        requestDtoCaregiver1 = CareMockTestInfo.getCareMatchingRequestDtoForCaregiver1();
        requestDtoCaregiver2 = CareMockTestInfo.getCareMatchingRequestDtoForCaregiver2();

        careMatching1 = CareMockTestInfo.getCareMatching1();
        careMatching2 = CareMockTestInfo.getCareMatching2();

        user1.updatePatient(patient1);
        user1.updateCareGiver(caregiver1);
        user2.updatePatient(patient2);
        user2.updateCareGiver(caregiver2);
    }

    @Test
    @DisplayName("간병인 - 환자 매칭 생성 테스트")
    void test1() {
        // Given
        given(userQuery.readById(authUser1.getUserId())).willReturn(user1);
        given(patientQuery.readById(user1.getPatient().getId())).willReturn(patient1);
        given(caregiverQuery.readById(requestDtoCaregiver2.getTargetId())).willReturn(caregiver2);
        given(careMatchingQuery.existsByActiveUserInCareMatching(patient1, caregiver2)).willReturn(false);
        given(careMatchingCommand.create(any(CareMatching.class))).willReturn(careMatching1);

        // When
        CareMatchingResponse result = careMatchingService.generate(authUser1, requestDtoCaregiver2);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getPatientSimpleResponseDto().getName()).isEqualTo(patient1.getName());
        assertThat(result.getCaregiverSimpleResponseDto().getName()).isEqualTo(caregiver1.getName());

        // Verify
        then(userQuery).should().readById(authUser1.getUserId());
        then(patientQuery).should().readById(patient1.getId());
        then(caregiverQuery).should().readById(requestDtoCaregiver2.getTargetId());
        then(careMatchingQuery).should().existsByActiveUserInCareMatching(patient1, caregiver2);
        then(careMatchingCommand).should().create(any(CareMatching.class));
    }

    @Test
    @DisplayName("간병인 매칭 목록 조회 테스트")
    void test2() {
        // Given
        given(userQuery.readById(authUser1.getUserId())).willReturn(user1);

        List<CareMatching> matchingList = List.of(careMatching1, careMatching2);
        Page<CareMatching> careMatchingPage = new PageImpl<>(matchingList, pageable, matchingList.size());

        given(careMatchingQuery.readPageByCaregiver(user1.getCaregiver(), pageable)).willReturn(careMatchingPage);

        // When
        Page<CareMatchingResponse> result = careMatchingService.findPageByCaregiver(authUser1, pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).getCaregiverSimpleResponseDto().getName()).isEqualTo(caregiver1.getName());
        assertThat(result.getContent().get(1).getCaregiverSimpleResponseDto().getName()).isEqualTo(caregiver2.getName());

        // Verify
        then(userQuery).should().readById(authUser1.getUserId());
        then(careMatchingQuery).should().readPageByCaregiver(user1.getCaregiver(), pageable);
    }

    @Test
    @DisplayName("환자 매칭 목록 조회 테스트")
    void test3() {
        // Given
        given(userQuery.readById(authUser2.getUserId())).willReturn(user2);

        List<CareMatching> matchingList = List.of(careMatching1, careMatching2);
        Page<CareMatching> careMatchingPage = new PageImpl<>(matchingList, pageable, matchingList.size());

        given(careMatchingQuery.readPageByPatient(user2.getPatient(), pageable)).willReturn(careMatchingPage);

        // When
        Page<CareMatchingResponse> result = careMatchingService.findPageByPatient(authUser2, pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).getPatientSimpleResponseDto().getName()).isEqualTo(patient1.getName());
        assertThat(result.getContent().get(1).getPatientSimpleResponseDto().getName()).isEqualTo(patient2.getName());
    }
}
