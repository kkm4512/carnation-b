package com.example.carnation.domain.careRecord.repository;

import com.example.carnation.domain.careRecord.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}
