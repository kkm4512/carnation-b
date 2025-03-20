package com.example.carnation.domain.care.service;

import com.example.carnation.common.dto.PageSearchDto;
import com.example.carnation.common.exception.CareException;
import com.example.carnation.common.response.enums.CareApiResponseEnum;
import com.example.carnation.domain.care.cqrs.PatientQuery;
import com.example.carnation.domain.care.dto.PatientIsMatchSearchDto;
import com.example.carnation.domain.care.dto.PatientRequestDto;
import com.example.carnation.domain.care.dto.PatientSimpleResponseDto;
import com.example.carnation.domain.care.entity.Patient;
import com.example.carnation.domain.user.common.cqrs.UserCommand;
import com.example.carnation.domain.user.common.cqrs.UserQuery;
import com.example.carnation.domain.user.common.entity.User;
import com.example.carnation.security.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j(topic = "PatientService")
@RequiredArgsConstructor
public class PatientService {
    private final UserQuery userQuery;
    private final UserCommand userCommand;
    private final PatientQuery patientQuery;

    // 환자 정보 저장
    @Transactional
    public void generate(final AuthUser authUser, final PatientRequestDto dto) {
        User user = userQuery.readById(authUser.getUserId());
        Patient patient = Patient.of(user, dto);
        user.updatePatient(patient);
        // 환자의 자격으로 구인공고에 올리려고하는데, 이미 간병인의 자격으로 구인공고 올린것이 존재한다면
        if (user.getCaregiver() != null && user.getCaregiver().getIsVisible().equals(Boolean.TRUE) && dto.getIsVisible().equals(Boolean.TRUE)) {
            throw new CareException(CareApiResponseEnum.DUPLICATE_JOB_POSTING_AS_CAREGIVER);
        }
        userCommand.create(user);
    }

    public Page<PatientSimpleResponseDto> findPageByAvailable(PatientIsMatchSearchDto dto) {
        Pageable pageable = PageSearchDto.of(dto);
        Page<Patient> result = patientQuery.readPageByAvailable(dto,pageable);
        return result.map(PatientSimpleResponseDto::of);
    }

    @Transactional
    public void modifyIsVisible(AuthUser authUser, Boolean IsVisible) {
        User user = userQuery.readById(authUser.getUserId());
        Patient patient = user.getPatient();
        user.isMe(patient.getUser());
        if (user.getCaregiver() != null && user.getCaregiver().getIsVisible().equals(Boolean.TRUE)) {
            throw new CareException(CareApiResponseEnum.DUPLICATE_JOB_POSTING_AS_CAREGIVER);
        }
        patient.updateIsVisible(IsVisible);
    }
}
