package com.example.carnation.domain.careRecord.cqrs;

import com.example.carnation.domain.careRecord.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
/**
 * 쓰기작업
 */
public class PatientQuery {
    private final PatientRepository repository;

}
