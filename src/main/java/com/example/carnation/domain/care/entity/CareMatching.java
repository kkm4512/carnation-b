package com.example.carnation.domain.care.entity;

import com.example.carnation.domain.care.constans.CareMatchingStatus;
import com.example.carnation.domain.care.dto.CareMatchingRequestDto;
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
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class CareMatching {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "간병 매칭 ID", example = "1")
    private Long id;

    @Column(nullable = false)
    @Schema(description = "간병 매칭 상태", example = "PENDING")
    @Enumerated(EnumType.STRING)
    private CareMatchingStatus matchStatus;

    @Column(nullable = false)
    @Schema(description = "간병인 -> 환자 Or 환자 -> 간병인에게 청구할 비용", example = "10000")
    private Integer amount;

    @CreatedDate
    @Column(updatable = false)
    @Schema(description = "간병 매칭 생성 시간", example = "2024-03-01T10:00:00")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column
    @Schema(description = "간병 매칭 수정 시간", example = "2024-03-02T15:30:00")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "caregiver_id", nullable = false)
    private Caregiver caregiver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Schema(description = "CareMatching에서 간병인이 작성한 간병 기록들")
    @OneToMany(mappedBy = "careMatching", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CareHistory> careHistories = new ArrayList<>();

    @Column(nullable = false)
    @Schema(description = "간병 시작 일자", example = "2024-03-01T10:00")
    private LocalDateTime startDateTime;

    @Column(nullable = false)
    @Schema(description = "간병 종료 일자", example = "2024-03-10T10:00")
    private LocalDateTime endDateTime;

    // careMatching이 처음 생성되는 생성자
    public CareMatching(Patient patient, Caregiver caregiver, Integer totalAmount, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.caregiver = caregiver;
        this.patient = patient;
        this.matchStatus = CareMatchingStatus.PENDING;
        this.amount = totalAmount;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public static CareMatching of(Patient patient, Caregiver caregiver, CareMatchingRequestDto dto) {
        return new CareMatching(
                patient,
                caregiver,
                dto.getTotalAmount(),
                dto.getStartDateTime(),
                dto.getEndDateTime()
        );
    }

    public void updateStatus(CareMatchingStatus matchingStatus) {
        this.matchStatus = matchingStatus;
    }

}
