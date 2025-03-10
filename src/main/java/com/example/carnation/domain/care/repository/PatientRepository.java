package com.example.carnation.domain.care.repository;

import com.example.carnation.domain.care.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}
