package com.example.carnation.domain.care.cqrs;

import com.example.carnation.domain.care.entity.CareAssignment;
import com.example.carnation.domain.care.repository.CareAssignmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CareAssignmentCommand {
    private final CareAssignmentRepository repository;

    @Transactional
    public CareAssignment save(CareAssignment entity) {
        return repository.save(entity);
    }
}
