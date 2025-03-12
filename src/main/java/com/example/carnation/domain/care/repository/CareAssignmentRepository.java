package com.example.carnation.domain.care.repository;

import com.example.carnation.domain.care.entity.CareAssignment;
import com.example.carnation.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CareAssignmentRepository extends JpaRepository<CareAssignment, Long> {
    Page<CareAssignment> findAllByUser(User user, Pageable pageable);
}
