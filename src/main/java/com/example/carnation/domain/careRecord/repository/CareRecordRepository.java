package com.example.carnation.domain.careRecord.repository;

import com.example.carnation.domain.careRecord.entity.CareRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CareRecordRepository extends JpaRepository<CareRecord, Long> {
}
