package com.example.carnation.domain.care.cqrs;

import com.example.carnation.common.exception.CareException;
import com.example.carnation.domain.care.dto.CareMatchingStatusSearchDto;
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
    public Page<CareMatching> readPageMeByCaregiver(Caregiver caregiver, Pageable pageable, CareMatchingStatusSearchDto dto) {
        return repository.findAllByCaregiver(caregiver,dto.getMatchStatus(), pageable);
    }

    @Transactional(readOnly = true)
    public Page<CareMatching> readPageMeByPatient(Patient patient, Pageable pageable, CareMatchingStatusSearchDto dto) {
        return repository.findAllByPatient(patient, dto.getMatchStatus(), pageable);
    }

    @Transactional(readOnly = true)
    public CareMatching readById(Long id) {
        return repository.findById(id).orElseThrow(() -> new CareException(NOT_FOUND));
    }

    /**
     * 특정 사용자가 (환자 또는 간병인) 기존의 **비활성화된 매칭 (CANCEL, END 상태)** 에 존재하는지 확인합니다.
     *
     * 🔹 **검사 조건**
     * - `matchStatus`가 `'CANCEL'` 또는 `'END'`인 매칭만 조회 → 즉, 종료된 매칭만 검사.
     * - `:patientId` 또는 `:caregiverId`가 `CareMatching`의 `patient.id` 또는 `caregiver.id` 중 하나와 일치하는지 확인.
     * - 해당 사용자가 기존 매칭에 속해 있는지 확인하여, 이후 새로운 매칭을 제한할 수 있음.
     *
     * 🔹 **예제 시나리오**
     * 1️⃣ (1,2) 매칭이 **종료됨** → (1,3) 새로운 매칭 가능 ✅
     * 2️⃣ (1,2) 매칭이 **취소됨** → (1,3) 새로운 매칭 가능 ✅
     * 3️⃣ (1,2) 매칭이 **진행 중** → (1,3) 매칭 제한 🚫 (이 쿼리는 진행 중인 매칭을 검사하지 않음)
     *
     */
    public Boolean existsByActiveUserInCareMatching(Patient patient, Caregiver caregiver) {
        return repository.existsByActiveUserInCareMatching(patient.getUser().getId(),caregiver.getUser().getId());
    }
}
