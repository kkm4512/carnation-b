package com.example.carnation.domain.care.entity;

import com.example.carnation.common.exception.CareException;
import com.example.carnation.common.response.enums.BaseApiResponseEnum;
import com.example.carnation.common.util.Validator;
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

    @Column(nullable = false)
    @Schema(description = "신장 (cm)", example = "170.5")
    private Double height;

    @Column(nullable = false)
    @Schema(description = "체중 (kg)", example = "65.0")
    private Double weight;

    @Column(nullable = false)
    @Schema(description = "간병인 공개 여부 (매칭 시스템에서 노출 여부)", example = "true")
    private Boolean isVisible;

    @CreatedDate
    @Column(updatable = false)
    @Schema(description = "생성일시", example = "2024-03-01T10:00:00")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column
    @Schema(description = "수정일시", example = "2024-03-01T10:00:00")
    private LocalDateTime updatedAt;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    public Caregiver(User user, String name, Double height, Double weight, Boolean isVisible) {
        this.user = user;
        this.name = name;
        this.height = height;
        this.weight = weight;
        this.isVisible = isVisible;
    }

    public static Caregiver of(User user, CaregiverRequestDto dto) {
        return new Caregiver(
                user,
                dto.getName(),
                dto.getHeight(),
                dto.getWeight(),
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
}
