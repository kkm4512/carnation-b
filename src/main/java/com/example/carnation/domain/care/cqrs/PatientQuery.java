package com.example.carnation.domain.care.cqrs;

import com.example.carnation.domain.care.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PatientQuery {
    private final PatientRepository repository;

    public long count() {
        return repository.count();
    }
}
