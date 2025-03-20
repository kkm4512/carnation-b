package com.example.carnation.domain.care.repository;

import com.example.carnation.domain.care.entity.Caregiver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CaregiverRepository extends JpaRepository<Caregiver, Long> {

    @Query("SELECT c FROM Caregiver c WHERE c.isVisible = TRUE " +
            "AND (:isMatch IS NULL OR c.isMatch = :isMatch)")
    Page<Caregiver> findAllByIsVisibleTrue(Boolean isMatch, Pageable pageable);


}
