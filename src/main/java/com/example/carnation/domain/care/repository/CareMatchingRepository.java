package com.example.carnation.domain.care.repository;

import com.example.carnation.domain.care.entity.CareMatching;
import com.example.carnation.domain.care.entity.Caregiver;
import com.example.carnation.domain.care.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CareMatchingRepository extends JpaRepository<CareMatching, Long> {
    Page<CareMatching> findAllByCaregiver(Caregiver caregiver, Pageable pageable);
    Page<CareMatching> findAllByPatient(Patient patient, Pageable pageable);
}
