package com.example.carnation.domain.care.entity;

import com.example.carnation.domain.care.dto.PatientRequestDto;
import com.example.carnation.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Schema(description = "Patient (환자) 엔티티")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "환자 ID", example = "1")
    private Long id;

    @Column(nullable = false, length = 30)
    @Schema(description = "환자 이름", example = "김철수")
    private String name;

    @Column(nullable = false, unique = true, length = 14)
    @Schema(description = "주민등록번호 (14자리)", example = "850101-2345678")
    private String residentRegistrationNumber;

    @CreatedDate
    @Column(updatable = false)
    @Schema(description = "생성일시", example = "2024-03-01T10:00:00")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column
    @Schema(description = "수정일시", example = "2024-03-02T15:30:00")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @Schema(description = "간병 배정을 작성한 사용자 (User)", example = "1")
    private User user;

    public Patient(User user, String name, String residentRegistrationNumber) {
        this.user = user;
        this.name = name;
        this.residentRegistrationNumber = residentRegistrationNumber;
    }

    public static Patient of(User user, PatientRequestDto dto) {
        return new Patient(
                user,
                dto.getName(),
                dto.getResidentRegistrationNumber()
        );
    }
}
