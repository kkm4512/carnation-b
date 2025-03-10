package com.example.carnation.domain.careRecord.entity;

import com.example.carnation.domain.careRecord.constans.RelationshipType;
import com.example.carnation.domain.careRecord.dto.CaregiverRequestDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Caregiver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "간병인 ID", example = "1")
    private Long id;

    @Column(nullable = false, length = 20)
    @Schema(description = "간병인 이름", example = "홍길동")
    private String name;

    @Column(nullable = false, unique = true, length = 14)
    @Schema(description = "주민등록번호 (14자리)", example = "900101-1234567")
    private String residentRegistrationNumber;

    @Column(nullable = false)
    @Schema(description = "신장 (cm)", example = "170.5")
    private Double height;

    @Column(nullable = false)
    @Schema(description = "체중 (kg)", example = "65.0")
    private Double weight;

    @Column(length = 5)
    @Schema(description = "혈액형", example = "O")
    private String bloodType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    @Schema(description = "피간병인과의 관계", example = "PARENT")
    private RelationshipType relationship;

    @Column(nullable = false, length = 15)
    @Schema(description = "전화번호", example = "010-1234-5678")
    private String phoneNumber;

    @Column(nullable = false)
    @Schema(description = "간병 시작일", example = "2024-03-01")
    private LocalDate startDate;

    @Column(nullable = false)
    @Schema(description = "간병 종료일", example = "2024-03-10")
    private LocalDate endDate;

    @CreatedDate
    @Column(updatable = false)
    @Schema(description = "생성일시", example = "2024-03-01T10:00:00")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column
    @Schema(description = "수정일시", example = "2024-03-01T10:00:00")
    private LocalDateTime updatedAt;

    public Caregiver(String name, String residentRegistrationNumber, Double height, Double weight, String bloodType, RelationshipType relationship, String phoneNumber, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.residentRegistrationNumber = residentRegistrationNumber;
        this.height = height;
        this.weight = weight;
        this.bloodType = bloodType;
        this.relationship = relationship;
        this.phoneNumber = phoneNumber;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static Caregiver of(CaregiverRequestDto caregiverDto) {
        return new Caregiver(
                caregiverDto.getName(),
                caregiverDto.getResidentRegistrationNumber(),
                caregiverDto.getHeight(),
                caregiverDto.getWeight(),
                caregiverDto.getBloodType(),
                caregiverDto.getRelationship(),
                caregiverDto.getPhoneNumber(),
                caregiverDto.getStartDate(),
                caregiverDto.getEndDate()
        );
    }
}
