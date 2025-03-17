package com.example.carnation.domain.care.cqrs;

import com.example.carnation.domain.care.entity.CareMatching;
import com.example.carnation.domain.care.repository.CareMatchingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CareMatchingCommand {
    private final CareMatchingRepository repository;

    @Transactional
    public CareMatching create(CareMatching entity) {
        return repository.save(entity);
    }
}
