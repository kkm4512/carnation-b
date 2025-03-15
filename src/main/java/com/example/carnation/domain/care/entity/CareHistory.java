package com.example.carnation.domain.care.entity;

import com.example.carnation.domain.care.dto.CareHistoryRequestDto;
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
@Schema(description = "간병 기록")
public class CareHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "간병 기록 ID", example = "1")
    private Long id;

    @Schema(description = "간병 내용", example = "환자에게 식사를 제공하고 산책을 도왔음")
    @Column(columnDefinition = "TEXT")
    private String text;


    @CreatedDate
    @Column(updatable = false)
    @Schema(description = "생성일시", example = "2024-03-01T10:00:00")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column
    @Schema(description = "수정일시", example = "2024-03-01T10:00:00")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "care_matching_id")
    @Schema(description = "간병 매칭 ID", example = "1")
    private CareMatching careMatching;

    @Schema(description = "S3에 저장된 미디어 URL 리스트")
    @OneToMany(mappedBy = "careHistory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CareMedia> medias = new ArrayList<>();

    public CareHistory(String text, CareMatching careMatching) {
        this.text = text;
        this.careMatching = careMatching;
    }

    public static CareHistory of(CareMatching careMatching, CareHistoryRequestDto dto) {
        return new CareHistory(
                dto.getText(),
                careMatching
        );
    }
}
