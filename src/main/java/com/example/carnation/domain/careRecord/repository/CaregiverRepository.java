package com.example.carnation.domain.careRecord.repository;

import com.example.carnation.domain.careRecord.entity.Caregiver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CaregiverRepository extends JpaRepository<Caregiver, Long> {
}
