package com.example.carnation.domain.care.repository;

import com.example.carnation.domain.care.entity.CareHistory;
import com.example.carnation.domain.care.entity.Caregiver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CareHistoryRepository extends JpaRepository<CareHistory, Long> {
    @Query("SELECT ch FROM CareHistory ch " +
            "JOIN ch.careMatching cm " +
            "WHERE cm.caregiver = :caregiver")
    Page<CareHistory> findAllByCaregiver(Caregiver caregiver, Pageable pageable);


}
