package com.example.carnation.common.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DatabaseResetRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Modifying
    public void resetAllAutoIncrement() {

        List<Object> results = entityManager.createNativeQuery(
                "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES " +
                        "WHERE TABLE_SCHEMA = DATABASE() AND AUTO_INCREMENT IS NOT NULL"
        ).getResultList();

        // 🔥 Object -> String 변환
        List<String> tableNames = results.stream()
                .map(Object::toString)
                .toList();

        for (String table : tableNames) {
            entityManager.createNativeQuery("ALTER TABLE " + table + " AUTO_INCREMENT = 1").executeUpdate();
        }
    }
}
