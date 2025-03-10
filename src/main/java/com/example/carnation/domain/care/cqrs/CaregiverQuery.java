package com.example.carnation.domain.care.cqrs;

import com.example.carnation.domain.care.repository.CaregiverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CaregiverQuery {
    private final CaregiverRepository repository;

    public long count() {
        return repository.count();
    }
}
