package com.example.carnation.domain.careRecord.cqrs;

import com.example.carnation.domain.careRecord.repository.CaregiverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CaregiverQuery {
    private final CaregiverRepository repository;

}
