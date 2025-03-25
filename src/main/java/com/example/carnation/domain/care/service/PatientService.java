package com.example.carnation.domain.care.service;

import com.example.carnation.common.dto.PageSearchDto;
import com.example.carnation.domain.care.cqrs.PatientQuery;
import com.example.carnation.domain.care.dto.PatientIsMatchSearchDto;
import com.example.carnation.domain.care.dto.PatientRequestDto;
import com.example.carnation.domain.care.dto.PatientSimpleResponseDto;
import com.example.carnation.domain.care.entity.Patient;
import com.example.carnation.domain.care.validate.CareValidate;
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
        CareValidate.validatePatientRegisterConflict(user,dto.getIsVisible());
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
        user.validateIsNotMe(patient.getUser());
        CareValidate.validatePatientRegisterConflict(user,IsVisible);
        patient.updateIsVisible(IsVisible);
    }
}
