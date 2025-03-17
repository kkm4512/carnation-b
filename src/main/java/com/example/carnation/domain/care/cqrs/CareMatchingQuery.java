package com.example.carnation.domain.care.cqrs;

import com.example.carnation.common.exception.CareException;
import com.example.carnation.domain.care.entity.CareMatching;
import com.example.carnation.domain.care.entity.Caregiver;
import com.example.carnation.domain.care.entity.Patient;
import com.example.carnation.domain.care.repository.CareMatchingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.carnation.common.response.enums.BaseApiResponseEnum.NOT_FOUND;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "CareAssignmentQuery")
public class CareMatchingQuery {
    private final CareMatchingRepository repository;

    @Transactional(readOnly = true)
    public long count() {
        return repository.count();
    }

    @Transactional(readOnly = true)
    public List<CareMatching> readAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<CareMatching> readPageByCaregiver(Caregiver caregiver, Pageable pageable) {
        return repository.findAllByCaregiver(caregiver, pageable);
    }

    @Transactional(readOnly = true)
    public Page<CareMatching> readPageByPatient(Patient patient, Pageable pageable) {
        return repository.findAllByPatient(patient, pageable);
    }

    @Transactional(readOnly = true)
    public CareMatching readById(Long id) {
        return repository.findById(id).orElseThrow(() -> new CareException(NOT_FOUND));
    }

    /**
     * 특정 사용자(Patient 또는 Caregiver)가 이미 활성화된 매칭(CareMatching)에 존재하는지 확인합니다.
     *
     * - `isMatch = true`인 매칭만 검사 (매칭이 종료된 경우는 무시)
     * - 사용자가 `patient` 또는 `caregiver`로 이미 존재하는 경우, 새로운 매칭을 방지
     * - 즉, 한 사용자가 동시에 **환자와 간병인 역할을 가질 수 없도록 제한**
     *
     * 예제:
     * 1. (1,2)가 존재할 때, (3,1) 추가 시 차단 (1번 사용자가 환자/간병인 역할을 바꿔가며 중복 매칭되는 것 방지)
     * 2. (1,2)와 (2,3)가 각각 존재할 때, (3,1) 추가 시 차단 (환자/간병인 관계 순서가 달라도 중복 차단)
     *
     * @param patient 새롭게 매칭하려는 환자의
     * @param caregiver 새롭게 매칭하려는 간병인
     * @return 이미 활성화된 매칭이 존재하면 true, 없으면 false
     */
    public Boolean existsByActiveUserInCareMatching(Patient patient, Caregiver caregiver) {
        return repository.existsByActiveUserInCareMatching(patient.getUser().getId(),caregiver.getUser().getId());
    }
}
