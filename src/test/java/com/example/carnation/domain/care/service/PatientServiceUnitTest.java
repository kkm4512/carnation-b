package com.example.carnation.domain.care.service;

import com.example.carnation.common.dto.constans.SortByEnum;
import com.example.carnation.domain.care.MockCareTestInfo;
import com.example.carnation.domain.care.cqrs.PatientQuery;
import com.example.carnation.domain.care.dto.PatientIsMatchSearchDto;
import com.example.carnation.domain.care.dto.PatientRequestDto;
import com.example.carnation.domain.care.dto.PatientSimpleResponseDto;
import com.example.carnation.domain.care.entity.Patient;
import com.example.carnation.domain.user.common.cqrs.UserCommand;
import com.example.carnation.domain.user.common.cqrs.UserQuery;
import com.example.carnation.domain.user.common.entity.User;
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

import static com.example.carnation.domain.user.MockUserInfo.getAuthUser1;
import static com.example.carnation.domain.user.MockUserInfo.getUser1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceUnitTest {

    @InjectMocks
    private PatientService patientService; // 테스트 대상

    @Mock
    private UserQuery userQuery;

    @Mock
    private UserCommand userCommand;

    @Mock
    private PatientQuery patientQuery;

    private AuthUser authUser;
    private User user;
    private PatientRequestDto patientRequestDto;
    private Pageable pageable;
    private Patient patient1;
    private Patient patient2;
    private final PatientIsMatchSearchDto dto = new PatientIsMatchSearchDto(true);

    @BeforeEach
    void setUp() {
        authUser = getAuthUser1();
        user = getUser1();

        // 환자 및 요청 DTO 생성
        patient1 = MockCareTestInfo.getPatient1();
        patient2 = MockCareTestInfo.getPatient2();
        patientRequestDto = MockCareTestInfo.getPatientRequestDto1();

        // Pageable 설정
        pageable = PageRequest.of(0, 10, Sort.by(SortByEnum.CREATED_AT.getDescription()).descending());
    }

    @Test
    @DisplayName("환자 정보 저장 테스트")
    void test1() {
        // Given
        given(userQuery.readById(authUser.getUserId())).willReturn(user);
        given(userCommand.create(any(User.class))).willReturn(user);

        // When
        patientService.generate(authUser, patientRequestDto);

        // Then
        assertThat(user.getPatient()).isNotNull();
        assertThat(user.getPatient().getName()).isEqualTo(patientRequestDto.getName());

        // Verify (메서드 호출 검증)
        then(userQuery).should().readById(authUser.getUserId());
        then(userCommand).should().create(user);
    }

    @Test
    @DisplayName("환자 목록 조회 - 여러 개의 환자 데이터 변환 테스트")
    void test2() {
        // Given
        List<Patient> patientList = List.of(patient1, patient2);
        Page<Patient> patientPage = new PageImpl<>(patientList, pageable, patientList.size());

        given(patientQuery.readPageByAvailable(dto,pageable)).willReturn(patientPage);

        // When
        Page<PatientSimpleResponseDto> result = patientService.findPageByAvailable(dto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).getName()).isEqualTo(patient1.getName());
        assertThat(result.getContent().get(1).getName()).isEqualTo(patient2.getName());

        // Verify
        then(patientQuery).should().readPageByAvailable(dto,pageable);
    }

    @Test
    @DisplayName("환자 목록 조회 - 데이터가 없을 때")
    void test3() {
        // Given
        Page<Patient> emptyPage = Page.empty();

        given(patientQuery.readPageByAvailable(dto,pageable)).willReturn(emptyPage);

        // When
        Page<PatientSimpleResponseDto> result = patientService.findPageByAvailable(dto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEmpty();

        // Verify
        then(patientQuery).should().readPageByAvailable(dto,pageable);
    }
}
