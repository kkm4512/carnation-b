package com.example.carnation.domain.careRecord.service;

import com.example.carnation.domain.careRecord.cqrs.*;
import com.example.carnation.domain.careRecord.dto.CaregiverRequestDto;
import com.example.carnation.domain.careRecord.dto.PatientRequestDto;
import com.example.carnation.domain.careRecord.entity.CareRecord;
import com.example.carnation.domain.careRecord.entity.Caregiver;
import com.example.carnation.domain.careRecord.entity.Patient;
import com.example.carnation.domain.user.entity.User;
import com.example.carnation.security.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "CareRecordService")
public class CareRecordService {
    private final CareRecordQuery careRecordQuery;
    private final CareRecordCommand careRecordCommand;
    private final CaregiverQuery caregiverQuery;
    private final CaregiverCommand caregiverCommand;
    private final PatientQuery patientQuery;
    private final PatientCommand patientCommand;

    public void create(final AuthUser authUser,final CaregiverRequestDto caregiverDto, final PatientRequestDto patientDto) {
        User user = User.of(authUser);
        Patient patient = Patient.of(patientDto);
        Caregiver caregiver = Caregiver.of(caregiverDto);
        CareRecord careRecord = CareRecord.of(user,patient,caregiver);
        careRecordCommand.save(careRecord);
    }
}
