package com.example.carnation.domain.care.service;

import com.example.carnation.domain.care.CareMockTestInfo;
import com.example.carnation.common.dto.constans.SortByEnum;
import com.example.carnation.domain.care.cqrs.CaregiverQuery;
import com.example.carnation.domain.care.dto.CaregiverRequestDto;
import com.example.carnation.domain.care.dto.CaregiverSimpleResponseDto;
import com.example.carnation.domain.care.entity.Caregiver;
import com.example.carnation.domain.care.entity.Patient;
import com.example.carnation.domain.user.cqrs.UserCommand;
import com.example.carnation.domain.user.cqrs.UserQuery;
import com.example.carnation.domain.user.dto.UserResponseDto;
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
class CaregiverServiceTest {

    @InjectMocks
    private CaregiverService caregiverService; // 테스트 대상

    @Mock
    private UserQuery userQuery;

    @Mock
    private UserCommand userCommand;

    @Mock
    private CaregiverQuery caregiverQuery;

    private AuthUser authUser1;
    private User user1;
    private CaregiverRequestDto caregiverRequestDto;
    private Pageable pageable;
    private Caregiver caregiver1;
    private Caregiver caregiver2;

    @BeforeEach
    void setUp() {
        authUser1 = CareMockTestInfo.getAuthUser1();
        user1 = CareMockTestInfo.getUser1(); // User 객체 생성

        // Patient 및 Caregiver 객체 생성 후, User에 설정
        Patient patient1 = CareMockTestInfo.getPatient1();
        Caregiver caregiver1 = CareMockTestInfo.getCaregiver1();
        user1.updatePatient(patient1);
        user1.updateCareGiver(caregiver1);
        caregiverRequestDto = CareMockTestInfo.getCaregiverRequestDto1();

        pageable = PageRequest.of(0, 10, Sort.by(SortByEnum.CREATED_AT.getDescription()).ascending());
        this.caregiver1 = CareMockTestInfo.getCaregiver1();
        caregiver2 = CareMockTestInfo.getCaregiver2();
    }

    @Test
    @DisplayName("간병인 저장 테스트")
    void test1() {
        // Given
        given(userQuery.readById(authUser1.getUserId())).willReturn(user1);
        given(userCommand.create(any(User.class))).willReturn(user1);

        // When
        UserResponseDto result = caregiverService.generate(authUser1, caregiverRequestDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getCaregiverSimpleResponseDto()).isNotNull();
        assertThat(result.getCaregiverSimpleResponseDto().getName()).isEqualTo(caregiverRequestDto.getName());

        // 🔥 추가된 검증: User 객체 내부의 Caregiver 확인
        assertThat(user1.getCaregiver()).isNotNull();
        assertThat(user1.getCaregiver().getName()).isEqualTo(caregiverRequestDto.getName());

        // Verify (메서드 호출 검증)
        then(userQuery).should().readById(authUser1.getUserId());
        then(userCommand).should().create(user1);
    }

    @Test
    @DisplayName("간병인 목록 조회 - 여러 개의 간병인 데이터 변환 테스트")
    void test2() {
        // Given
        List<Caregiver> caregiverList = List.of(caregiver1, caregiver2);
        Page<Caregiver> caregiverPage = new PageImpl<>(caregiverList, pageable, caregiverList.size());

        given(caregiverQuery.readPageByAvailable(pageable)).willReturn(caregiverPage);

        // When
        Page<CaregiverSimpleResponseDto> result = caregiverService.findPageByAvailable(pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).getName()).isEqualTo(caregiver1.getName());
        assertThat(result.getContent().get(1).getName()).isEqualTo(caregiver2.getName());

        // Verify
        then(caregiverQuery).should().readPageByAvailable(pageable);
    }

    @Test
    @DisplayName("간병인 목록 조회 - 데이터가 없을 때")
    void test3() {
        // Given
        Page<Caregiver> emptyPage = Page.empty();

        given(caregiverQuery.readPageByAvailable(pageable)).willReturn(emptyPage);

        // When
        Page<CaregiverSimpleResponseDto> result = caregiverService.findPageByAvailable(pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEmpty();

        // Verify
        then(caregiverQuery).should().readPageByAvailable(pageable);
    }
}
