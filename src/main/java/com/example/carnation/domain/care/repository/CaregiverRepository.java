package com.example.carnation.domain.care.repository;

import com.example.carnation.domain.care.entity.Caregiver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CaregiverRepository extends JpaRepository<Caregiver, Long> {
}
