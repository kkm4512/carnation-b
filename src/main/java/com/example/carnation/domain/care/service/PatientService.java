package com.example.carnation.domain.care.service;

import com.example.carnation.domain.care.cqrs.PatientQuery;
import com.example.carnation.domain.care.dto.PatientRequestDto;
import com.example.carnation.domain.care.entity.Patient;
import com.example.carnation.domain.user.cqrs.UserCommand;
import com.example.carnation.domain.user.cqrs.UserQuery;
import com.example.carnation.domain.user.entity.User;
import com.example.carnation.security.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public void create(final AuthUser authUser, final PatientRequestDto dto) {
        User user = userQuery.readById(authUser.getUserId());
        Patient patient = Patient.of(user, dto);
        user.updatePatient(patient);
        userCommand.create(user);
    }
}
