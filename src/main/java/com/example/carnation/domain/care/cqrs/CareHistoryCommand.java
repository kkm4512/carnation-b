package com.example.carnation.domain.care.cqrs;

import com.example.carnation.domain.care.entity.CareHistory;
import com.example.carnation.domain.care.repository.CareHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CareHistoryCommand {
    private final CareHistoryRepository repository;

    @Transactional
    public CareHistory create(CareHistory entity) {
        return repository.save(entity);
    }
}
