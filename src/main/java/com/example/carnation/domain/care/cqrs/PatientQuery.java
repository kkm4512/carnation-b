package com.example.carnation.domain.care.cqrs;

import com.example.carnation.common.exception.CareException;
import com.example.carnation.common.response.enums.CareApiResponseEnum;
import com.example.carnation.domain.care.dto.PatientIsMatchSearchDto;
import com.example.carnation.domain.care.entity.Patient;
import com.example.carnation.domain.care.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PatientQuery {
    private final PatientRepository repository;

    @Transactional(readOnly = true)
    public long count() {
        return repository.count();
    }

    @Transactional(readOnly = true)
    public Page<Patient> readPageByAvailable(PatientIsMatchSearchDto dto,Pageable pageable) {
        return repository.findAllByIsVisibleTrue(dto.getIsMatch(), pageable);
    }

    public Patient readById(Long id) {
        return repository.findById(id).orElseThrow(() -> new CareException(CareApiResponseEnum.PATIENT_NOT_FOUND));
    }
}
