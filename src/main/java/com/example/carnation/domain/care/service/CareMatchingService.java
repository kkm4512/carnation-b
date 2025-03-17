package com.example.carnation.domain.care.service;

import com.example.carnation.domain.care.constans.UserType;
import com.example.carnation.domain.care.cqrs.CareMatchingCommand;
import com.example.carnation.domain.care.cqrs.CareMatchingQuery;
import com.example.carnation.domain.care.cqrs.CaregiverQuery;
import com.example.carnation.domain.care.cqrs.PatientQuery;
import com.example.carnation.domain.care.dto.CareMatchingRequestDto;
import com.example.carnation.domain.care.dto.CareMatchingResponse;
import com.example.carnation.domain.care.entity.CareMatching;
import com.example.carnation.domain.care.entity.Caregiver;
import com.example.carnation.domain.care.entity.Patient;
import com.example.carnation.domain.care.validate.CareValidate;
import com.example.carnation.domain.user.cqrs.UserQuery;
import com.example.carnation.domain.user.entity.User;
import com.example.carnation.security.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CareMatchingService {
    private final CareMatchingQuery careMatchingQuery;
    private final CareMatchingCommand careMatchingCommand;
    private final CaregiverQuery caregiverQuery;
    private final PatientQuery patientQuery;
    private final UserQuery userQuery;

    // 간병인 - 환자 생성 비즈니스 로직
    public CareMatchingResponse generate(final AuthUser authUser, final CareMatchingRequestDto dto) {
        User user = userQuery.readById(authUser.getUserId());
        Patient patient = null;
        Caregiver caregiver = null;
        if (dto.getUserType().equals(UserType.CAREGIVER)) {
            patient = patientQuery.readById(user.getPatient().getId());
            caregiver = caregiverQuery.readById(dto.getTargetId());
        }
        if (dto.getUserType().equals(UserType.PATIENT)) {
            patient = patientQuery.readById(dto.getTargetId());
            caregiver = caregiverQuery.readById(user.getCaregiver().getId());
        }
        CareValidate.validateSelfCareMatching(patient, caregiver);
        Boolean isCareMatching = careMatchingQuery.existsByActiveUserInCareMatching(patient, caregiver);
        CareValidate.validateCareMatching(isCareMatching);
        CareMatching careMatching = CareMatching.of(patient, caregiver, dto);
        CareMatching saveCareMatching = careMatchingCommand.create(careMatching);
        return CareMatchingResponse.of(saveCareMatching);
    }


    // 로그인해 있는 유저의 간병인으로 매칭되있는것들을 가져옴
    public Page<CareMatchingResponse> findPageByCaregiver(final AuthUser authUser, final Pageable pageable) {
        User user = userQuery.readById(authUser.getUserId());
        Page<CareMatching> responses = careMatchingQuery.readPageByCaregiver(user.getCaregiver(),pageable);
        return responses.map(CareMatchingResponse::of);
    }

    // 로그인해 있는 유저의 환자로 매칭되있는것들을 가져옴
    public Page<CareMatchingResponse> findPageByPatient(final AuthUser authUser, final Pageable pageable) {
        User user = userQuery.readById(authUser.getUserId());
        Page<CareMatching> responses = careMatchingQuery.readPageByPatient(user.getPatient(),pageable);
        return responses.map(CareMatchingResponse::of);
    }

}
