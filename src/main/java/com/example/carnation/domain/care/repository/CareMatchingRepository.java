package com.example.carnation.domain.care.repository;

import com.example.carnation.domain.care.constans.CareMatchingStatus;
import com.example.carnation.domain.care.entity.CareMatching;
import com.example.carnation.domain.care.entity.Caregiver;
import com.example.carnation.domain.care.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CareMatchingRepository extends JpaRepository<CareMatching, Long> {
    @Query("SELECT c FROM CareMatching c WHERE c.caregiver = :caregiver " +
            "AND (:matchStatus IS NULL OR c.matchStatus = :matchStatus)")
    Page<CareMatching> findAllByCaregiver(Caregiver caregiver, CareMatchingStatus matchStatus, Pageable pageable);

    @Query("SELECT c FROM CareMatching c WHERE c.patient = :patient " +
            "AND (:matchStatus IS NULL OR c.matchStatus = :matchStatus)")
    Page<CareMatching> findAllByPatient(Patient patient, CareMatchingStatus matchStatus, Pageable pageable);
    @Query("SELECT CASE WHEN EXISTS (" +
            "SELECT 1 FROM CareMatching c WHERE " +
            "c.matchStatus IN ('MATCHING', 'PENDING') AND " +
            "(:patientId IN (c.patient.id, c.caregiver.id) OR " +
            ":caregiverId IN (c.patient.id, c.caregiver.id))) " +
            "THEN TRUE ELSE FALSE END")
    Boolean existsByActiveUserInCareMatching(Long patientId, Long caregiverId);





}
