package com.example.carnation.domain.care.repository;

import com.example.carnation.domain.care.entity.CareHistory;
import com.example.carnation.domain.care.entity.Caregiver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CareHistoryRepository extends JpaRepository<CareHistory, Long> {
    Page<CareHistory> findAllByCaregiver(Caregiver caregiver, Pageable pageable);

}
