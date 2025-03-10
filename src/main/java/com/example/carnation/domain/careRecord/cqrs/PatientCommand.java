package com.example.carnation.domain.careRecord.cqrs;

import com.example.carnation.domain.careRecord.entity.Patient;
import com.example.carnation.domain.careRecord.repository.PatientRepository;
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
