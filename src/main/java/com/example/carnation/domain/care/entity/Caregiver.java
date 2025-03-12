package com.example.carnation.domain.care.entity;

import com.example.carnation.domain.care.constans.RelationshipType;
import com.example.carnation.domain.care.dto.CaregiverRequestDto;
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
import java.util.ArrayList;
import java.util.List;

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    @Schema(description = "피간병인과의 관계", example = "PARENT")
    private RelationshipType relationship;

    @Column(nullable = false, length = 15)
    @Schema(description = "전화번호", example = "010-1234-5678")
    private String phoneNumber;

    @Column(nullable = false)
    @Schema(description = "간병 시작일", example = "2024-03-01T10:00")
    private LocalDateTime startDateTime;

    @Column(nullable = false)
    @Schema(description = "간병 종료일", example = "2024-03-10T10:00")
    private LocalDateTime endDateTime;


    @CreatedDate
    @Column(updatable = false)
    @Schema(description = "생성일시", example = "2024-03-01T10:00:00")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column
    @Schema(description = "수정일시", example = "2024-03-01T10:00:00")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @Schema(description = "간병 배정을 작성한 사용자 (User)", example = "1")
    private User user;

    @Schema(description = "간병인이 작성한 간병 기록들")
    @OneToMany(mappedBy = "caregiver", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CareHistory> careHistories = new ArrayList<>();

    public Caregiver(User user, String name, String residentRegistrationNumber, Double height, Double weight, RelationshipType relationship, String phoneNumber, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.user = user;
        this.name = name;
        this.residentRegistrationNumber = residentRegistrationNumber;
        this.height = height;
        this.weight = weight;
        this.relationship = relationship;
        this.phoneNumber = phoneNumber;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public static Caregiver of(User user, CaregiverRequestDto dto) {
        return new Caregiver(
                user,
                dto.getName(),
                dto.getResidentRegistrationNumber(),
                dto.getHeight(),
                dto.getWeight(),
                dto.getRelationship(),
                dto.getPhoneNumber(),
                dto.getStartDateTime(),
                dto.getEndDateTime()
        );
    }
}
