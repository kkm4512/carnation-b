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
    private final CareAssignmentQuery careAssignmentQuery;
    private final CareAssignmentCommand careAssignmentCommand;

    public CareAssignmentResponse create(final AuthUser authUser, final CaregiverRequestDto caregiverDto, final PatientRequestDto patientDto) {
        User user = User.of(authUser);
        log.info("[간병 배정 생성 시작] 요청자: userId={}", user.getId());

        try {
            Patient patient = Patient.of(user, patientDto);
            Caregiver caregiver = Caregiver.of(user, caregiverDto);
            CareAssignment careAssignment = CareAssignment.of(user, patient, caregiver);

            CareAssignmentResponse response = CareAssignmentResponse.of(careAssignmentCommand.save(careAssignment));
            log.info("[간병 배정 생성 완료] careAssignmentId={}, patientId={}, caregiverId={}", response.getId(), patient.getId(), caregiver.getId());

            return response;
        } catch (Exception e) {
            log.error("[간병 배정 생성 실패] userId={}, error={}", user.getId(), e.getMessage(), e);
            throw e;
        }
    }

    public Page<CareAssignmentResponse> readAllMePage(final AuthUser authUser, final Pageable pageable) {
        User user = User.of(authUser);
        log.info("[간병 배정 목록 조회 시작] 요청자: userId={}", user.getId());

        try {
            Page<CareAssignment> responses = careAssignmentQuery.readAllMePage(user, pageable);
            log.info("[간병 배정 목록 조회 완료] userId={}, 조회된 개수={}", user.getId(), responses.getTotalElements());

            return responses.map(CareAssignmentResponse::of);
        } catch (Exception e) {
            log.error("[간병 배정 목록 조회 실패] userId={}, error={}", user.getId(), e.getMessage(), e);
            throw e;
        }
    }

}
