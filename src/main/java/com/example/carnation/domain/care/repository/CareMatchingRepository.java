package com.example.carnation.domain.care.repository;

import com.example.carnation.domain.care.entity.CareMatching;
import com.example.carnation.domain.care.entity.Caregiver;
import com.example.carnation.domain.care.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CareMatchingRepository extends JpaRepository<CareMatching, Long> {
    Page<CareMatching> findAllByCaregiver(Caregiver caregiver, Pageable pageable);
    Page<CareMatching> findAllByPatient(Patient patient, Pageable pageable);
    @Query("SELECT COUNT(c) > 0 FROM CareMatching c WHERE " +
            "c.isMatch = true AND " +  // 🔹 활성화된 매칭만 검사
            "(:patientId IN (c.patient.id, c.caregiver.id) OR " +
            ":caregiverId IN (c.patient.id, c.caregiver.id))")
    Boolean existsByActiveUserInCareMatching(Long patientId, Long caregiverId);




}
