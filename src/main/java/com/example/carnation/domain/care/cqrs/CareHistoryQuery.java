package com.example.carnation.domain.care.cqrs;

import com.example.carnation.domain.care.entity.CareHistory;
import com.example.carnation.domain.care.entity.Caregiver;
import com.example.carnation.domain.care.repository.CareHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CareHistoryQuery {
    private final CareHistoryRepository repository;

    @Transactional(readOnly = true)
    public long count() {
        return repository.count();
    }

    @Transactional(readOnly = true)
    public List<CareHistory> readAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<CareHistory> readPageByMe(Caregiver caregiver, Pageable pageable) {
        return repository.findAllByCaregiver(caregiver,pageable);
    }
}
