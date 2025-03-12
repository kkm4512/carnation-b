package com.example.carnation.domain.care.cqrs;

import com.example.carnation.common.exception.CareException;
import com.example.carnation.domain.care.entity.CareAssignment;
import com.example.carnation.domain.care.repository.CareAssignmentRepository;
import com.example.carnation.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.carnation.common.response.enums.BaseApiResponse.NOT_FOUND;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "CareAssignmentQuery")
public class CareAssignmentQuery {
    private final CareAssignmentRepository repository;

    @Transactional(readOnly = true)
    public long count() {
        return repository.count();
    }

    @Transactional(readOnly = true)
    public List<CareAssignment> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<CareAssignment> readAllMePage(User user, Pageable pageable) {
        return repository.findAllByUser(user, pageable);
    }

    @Transactional(readOnly = true)
    public CareAssignment findOne(Long id) {
        return repository.findById(id).orElseThrow(() -> new CareException(NOT_FOUND));
    }
}
