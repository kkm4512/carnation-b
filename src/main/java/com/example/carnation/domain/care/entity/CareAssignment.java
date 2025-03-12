package com.example.carnation.domain.care.entity;

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
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class CareAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "간병 배정 ID", example = "1")
    private Long id;

    @CreatedDate
    @Column(updatable = false)
    @Schema(description = "간병 배정 생성 시간", example = "2024-03-01T10:00:00")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column
    @Schema(description = "간병 배정 수정 시간", example = "2024-03-02T15:30:00")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @Schema(description = "간병 배정을 작성한 사용자 (User)", example = "1")
    private User user;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "patient_id", nullable = false, unique = true)
    @Schema(description = "간병 배정 피간병인 (Patient)", example = "1")
    private Patient patient;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "caregiver_id", nullable = false, unique = true)
    @Schema(description = "간병 배정 간병인 (Caregiver)", example = "1")
    private Caregiver caregiver;

    public CareAssignment(User user, Patient patient, Caregiver caregiver) {
        this.user = user;
        this.patient = patient;
        this.caregiver = caregiver;
    }

    public static CareAssignment of(User user, Patient patient, Caregiver caregiver) {
        return new CareAssignment(user, patient, caregiver);
    }
}
