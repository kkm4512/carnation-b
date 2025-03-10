package com.example.carnation.domain.careRecord.cqrs;

import com.example.carnation.domain.careRecord.entity.CareRecord;
import com.example.carnation.domain.careRecord.repository.CareRecordRepository;
import com.example.carnation.domain.user.entity.User;
import com.example.carnation.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CareRecordCommand {
    private final CareRecordRepository repository;

    @Transactional
    public CareRecord save(CareRecord entity) {
        return repository.save(entity);
    }
}
