package com.example.carnation.domain.care.cqrs;

import com.example.carnation.domain.care.entity.CareHistory;
import com.example.carnation.domain.care.repository.CareHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CareHistoryQuery {
    private final CareHistoryRepository repository;

    public long count() {
        return repository.count();
    }

    public List<CareHistory> findAll() {
        return repository.findAll();
    }
}
