package com.example.carnation.domain.care.repository;

import com.example.carnation.domain.care.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    @Query("SELECT p FROM Patient p WHERE p.isVisible = TRUE " +
            "AND (:isMatch IS NULL OR p.isMatch = :isMatch)")
    Page<Patient> findAllByIsVisibleTrue(Boolean isMatch, Pageable pageable);
}
