package com.example.carnation.domain.care.cqrs;

import com.example.carnation.domain.care.entity.Patient;
import com.example.carnation.domain.care.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PatientCommand {
    private final PatientRepository repository;

    @Transactional
    public Patient save(Patient entity) {
        return repository.save(entity);
    }
}
