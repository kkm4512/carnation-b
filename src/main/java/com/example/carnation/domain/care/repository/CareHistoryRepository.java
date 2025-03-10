package com.example.carnation.domain.care.repository;

import com.example.carnation.domain.care.entity.CareHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CareHistoryRepository extends JpaRepository<CareHistory, Long> {
}
