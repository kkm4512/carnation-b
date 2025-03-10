package com.example.carnation.domain.care.service;

import com.example.carnation.domain.care.cqrs.CareAssignmentCommand;
import com.example.carnation.domain.care.cqrs.CareAssignmentQuery;
import com.example.carnation.domain.care.dto.CareAssignmentResponse;
import com.example.carnation.domain.care.dto.CaregiverRequestDto;
import com.example.carnation.domain.care.dto.PatientRequestDto;
import com.example.carnation.domain.care.entity.CareAssignment;
import com.example.carnation.domain.care.entity.Caregiver;
import com.example.carnation.domain.care.entity.Patient;
import com.example.carnation.domain.user.entity.User;
import com.example.carnation.security.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "CareRecordService")
public class CareAssignmentService {
    private final CareAssignmentQuery careRecordQuery;
    private final CareAssignmentCommand careRecordCommand;

    public void create(final AuthUser authUser,final CaregiverRequestDto caregiverDto, final PatientRequestDto patientDto) {
        User user = User.of(authUser);
        Patient patient = Patient.of(user, patientDto);
        Caregiver caregiver = Caregiver.of(user, caregiverDto);
        CareAssignment careAssignment = CareAssignment.of(user,patient,caregiver);
        careRecordCommand.save(careAssignment);
    }

    public Page<CareAssignmentResponse> readAllMe(final AuthUser authUser, final Pageable pageable) {
        User user = User.of(authUser);
        Page<CareAssignment> responses = careRecordQuery.readAllMe(user, pageable);
        return responses.map(CareAssignmentResponse::of);
    }
}
