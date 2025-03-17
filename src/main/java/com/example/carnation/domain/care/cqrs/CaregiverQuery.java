package com.example.carnation.domain.care.cqrs;

import com.example.carnation.common.exception.CareException;
import com.example.carnation.common.response.enums.CareApiResponseEnum;
import com.example.carnation.domain.care.entity.Caregiver;
import com.example.carnation.domain.care.repository.CaregiverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CaregiverQuery {
    private final CaregiverRepository repository;

    @Transactional(readOnly = true)
    public long count() {
        return repository.count();
    }

    @Transactional(readOnly = true)
    public Page<Caregiver> readPageByAvailable(Pageable pageable) {
        return repository.findAllByIsVisibleTrue(pageable);
    }

    public Caregiver readById(Long id) {
        return repository.findById(id).orElseThrow(() -> new CareException(CareApiResponseEnum.CAREGIVER_NOT_FOUND));
    }
}
