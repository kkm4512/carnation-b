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
    public Page<CareMatching> readPageCaregiver(Caregiver caregiver, Pageable pageable) {
        return repository.findAllByCaregiver(caregiver, pageable);
    }

    @Transactional(readOnly = true)
    public Page<CareMatching> readPagePatient(Patient patient, Pageable pageable) {
        return repository.findAllByPatient(patient, pageable);
    }

    @Transactional(readOnly = true)
    public CareMatching readById(Long id) {
        return repository.findById(id).orElseThrow(() -> new CareException(NOT_FOUND));
    }
}
