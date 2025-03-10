package com.example.carnation.domain.careRecord.cqrs;

import com.example.carnation.domain.careRecord.entity.Caregiver;
import com.example.carnation.domain.careRecord.repository.CaregiverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CaregiverCommand {
    private final CaregiverRepository repository;

    @Transactional
    public Caregiver save(Caregiver entity) {
        return repository.save(entity);
    }
}
