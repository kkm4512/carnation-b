package com.example.carnation.domain.care.service;

import com.example.carnation.common.dto.constans.SortByEnum;
import com.example.carnation.domain.care.MockCareTestInfo;
import com.example.carnation.domain.care.cqrs.CaregiverQuery;
import com.example.carnation.domain.care.dto.CaregiverIsMatchSearchDto;
import com.example.carnation.domain.care.dto.CaregiverRequestDto;
import com.example.carnation.domain.care.dto.CaregiverSimpleResponseDto;
import com.example.carnation.domain.care.entity.Caregiver;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class CaregiverServiceUnitTest {

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
    private final CaregiverIsMatchSearchDto dto = new CaregiverIsMatchSearchDto(true);

    @BeforeEach
    void setUp() {
        authUser1 = getAuthUser1();
        user1 = getUser1(); // User 객체 생성

        // Patient 및 Caregiver 객체 생성 후, User에 설정
        Patient patient1 = MockCareTestInfo.getPatient1();
        Caregiver caregiver1 = MockCareTestInfo.getCaregiver1();
        user1.updatePatient(patient1);
        user1.updateCareGiver(caregiver1);
        caregiverRequestDto = MockCareTestInfo.getCaregiverRequestDto1();

        pageable = PageRequest.of(0, 10, Sort.by(SortByEnum.CREATED_AT.getDescription()).descending());
        this.caregiver1 = MockCareTestInfo.getCaregiver1();
        caregiver2 = MockCareTestInfo.getCaregiver2();
    }

    @Test
    @DisplayName("간병인 저장 실패 테스트 - 예외 발생 시 성공")
    void test1() {
        // Given
        given(userQuery.readById(authUser1.getUserId())).willReturn(user1);

        // When & Then
        assertThrows(Exception.class, () -> {
            caregiverService.generate(authUser1, caregiverRequestDto);
        });
    }


    @Test
    @DisplayName("간병인 목록 조회 - 여러 개의 간병인 데이터 변환 테스트")
    void test2() {
        // Given
        List<Caregiver> caregiverList = List.of(caregiver1, caregiver2);
        Page<Caregiver> caregiverPage = new PageImpl<>(caregiverList, pageable, caregiverList.size());


        given(caregiverQuery.readPageByAvailable(dto, pageable)).willReturn(caregiverPage);

        // When
        Page<CaregiverSimpleResponseDto> result = caregiverService.findPageByAvailable(dto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).getName()).isEqualTo(caregiver1.getName());
        assertThat(result.getContent().get(1).getName()).isEqualTo(caregiver2.getName());

        // Verify
        then(caregiverQuery).should().readPageByAvailable(dto,pageable);
    }

    @Test
    @DisplayName("간병인 목록 조회 - 데이터가 없을 때")
    void test3() {
        // Given
        Page<Caregiver> emptyPage = Page.empty();

        given(caregiverQuery.readPageByAvailable(dto, pageable)).willReturn(emptyPage);

        // When
        Page<CaregiverSimpleResponseDto> result = caregiverService.findPageByAvailable(dto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEmpty();

        // Verify
        then(caregiverQuery).should().readPageByAvailable(dto, pageable);
    }
}
