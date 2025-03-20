package com.example.carnation.domain.care.entity;

import com.example.carnation.common.exception.CareException;
import com.example.carnation.common.response.enums.BaseApiResponseEnum;
import com.example.carnation.common.util.Validator;
import com.example.carnation.domain.care.dto.PatientRequestDto;
import com.example.carnation.domain.user.common.entity.User;
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
import java.util.List;

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

    @Column(nullable = false, length = 100)
    @Schema(description = "환자가 있는 장소", example = "서울특별시 강남구 삼성동")
    private String location;

    @Column(nullable = false, length = 100)
    @Schema(description = "환자의 병명", example = "고혈압, 당뇨병")
    private String diagnosis;

    @Column(nullable = false)
    @Schema(description = "환자 공개 여부 (매칭 시스템에서 노출 여부)", example = "true")
    private Boolean isVisible;

    @Column(nullable = false)
    @Schema(description = "간병 매칭 가능 상태", example = "true")
    private Boolean isMatch;

    @CreatedDate
    @Column(updatable = false)
    @Schema(description = "생성일시", example = "2024-03-01T10:00:00")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column
    @Schema(description = "수정일시", example = "2024-03-02T15:30:00")
    private LocalDateTime updatedAt;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    @Schema(description = "환자와 연결된 사용자 ID", example = "5")
    private User user;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CareMatching> careMatchings;

    // 첫 생성시
    public Patient(User user, String name, String location, String diagnosis, Boolean isVisible) {
        this.isMatch = true;
        this.user = user;
        this.name = name;
        this.location = location;
        this.diagnosis = diagnosis;
        this.isVisible = isVisible;
    }

    public static Patient of(User user, PatientRequestDto dto) {
        return new Patient(
                user,
                dto.getName(),
                dto.getLocation(),
                dto.getDiagnosis(),
                dto.getIsVisible()
        );
    }

    public void isMe(User user){
        Validator.validateNotNullAndEqual(
                this.getUser().getId(),
                user.getId(),
                new CareException(BaseApiResponseEnum.RESOURCE_NOT_OWNED)
        );
    }

    public void updateIsMatch(Boolean isMatch) {
        this.isMatch = isMatch;
    }

    public void updateIsVisible(Boolean isVisible) {
        this.isVisible = isVisible;
    }
}
