package com.example.carnation.domain.careRecord.cqrs;

import com.example.carnation.domain.careRecord.repository.CareRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CareRecordQuery {
    private final CareRecordRepository repository;

}
