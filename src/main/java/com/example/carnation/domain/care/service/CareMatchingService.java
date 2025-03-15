package com.example.carnation.domain.care.service;

import com.example.carnation.common.exception.CareException;
import com.example.carnation.common.response.enums.BaseApiResponseEnum;
import com.example.carnation.common.util.Validator;
import com.example.carnation.domain.care.cqrs.CareMatchingCommand;
import com.example.carnation.domain.care.cqrs.CareMatchingQuery;
import com.example.carnation.domain.care.cqrs.CaregiverQuery;
import com.example.carnation.domain.care.cqrs.PatientQuery;
import com.example.carnation.domain.care.dto.CareMatchingRequestDto;
import com.example.carnation.domain.care.dto.CareMatchingResponse;
import com.example.carnation.domain.care.dto.CaregiverSimpleResponseDto;
import com.example.carnation.domain.care.dto.PatientSimpleResponseDto;
import com.example.carnation.domain.care.entity.CareMatching;
import com.example.carnation.domain.care.entity.Caregiver;
import com.example.carnation.domain.care.entity.Patient;
import com.example.carnation.domain.user.cqrs.UserQuery;
import com.example.carnation.domain.user.entity.User;
import com.example.carnation.security.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CareMatchingService {
    private final CareMatchingQuery careMatchingQuery;
    private final CareMatchingCommand careMatchingCommand;
    private final CaregiverQuery caregiverQuery;
    private final PatientQuery patientQuery;
    private final UserQuery userQuery;

    // 간병인 - 환자 생성해주는 비즈니스 로직
    @Transactional
    public CareMatchingResponse generate(final CareMatchingRequestDto dto) {
        Patient patient = patientQuery.readById(dto.getPatientId());
        Caregiver caregiver = caregiverQuery.readById(dto.getCaregiverId());
        Validator.validateNotNullAndNotEqual(patient.getUser().getId(),caregiver.getUser().getId(), new CareException(BaseApiResponseEnum.SELF_CARE_ASSIGNMENT_NOT_ALLOWED));
        CareMatching careMatching = CareMatching.of(patient, caregiver,dto);
        CareMatching saveCareMatching = careMatchingCommand.create(careMatching);
        return CareMatchingResponse.of(saveCareMatching);
    }

    @Transactional(readOnly = true)
    public Page<CareMatchingResponse> findPageByCaregiver(final AuthUser authUser, final Pageable pageable) {
        User user = userQuery.readById(authUser.getUserId());
        Page<CareMatching> responses = careMatchingQuery.readPageCaregiver(user.getCaregiver(),pageable);
        return responses.map(CareMatchingResponse::of);
    }

    @Transactional(readOnly = true)
    public Page<CaregiverSimpleResponseDto> findPageAvailableByCaregiver(final Pageable pageable) {
        Page<Caregiver> result = caregiverQuery.readPageAvailable(pageable);
        return result.map(CaregiverSimpleResponseDto::of);
    }

    @Transactional(readOnly = true)
    public Page<CareMatchingResponse> findPageByPatient(final AuthUser authUser, final Pageable pageable) {
        User user = userQuery.readById(authUser.getUserId());
        Page<CareMatching> responses = careMatchingQuery.readPagePatient(user.getPatient(),pageable);
        return responses.map(CareMatchingResponse::of);
    }

    @Transactional(readOnly = true)
    public Page<PatientSimpleResponseDto> findPageAvailableByPatient(final Pageable pageable) {
        Page<Patient> result = patientQuery.readPageAvailable(pageable);
        return result.map(PatientSimpleResponseDto::of);
    }


}
