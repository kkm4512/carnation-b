package com.example.carnation.domain.care.entity;

import com.example.carnation.domain.care.constans.MediaType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Schema(description = "간병인이 업로드한 미디어 파일")
@Builder
public class CareMedia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "간병인이 작성한 사진, 동영상 ID", example = "1")
    private Long id;

    @Schema(description = "저장된 File 상대경로 ", example = "s3.example.com/file1.jpg")
    @Column(nullable = false)
    private String filePath;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String fileOriginName;

    @Column(nullable = false)
    private String fileSize;

    @Enumerated(EnumType.STRING)
    @Schema(description = "파일 타입 (IMAGE 또는 VIDEO)", example = "IMAGE")
    @Column(nullable = false, length = 10)
    private MediaType mediaType;

    @CreatedDate
    @Column(updatable = false)
    @Schema(description = "생성일시", example = "2024-03-01T10:00:00")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column
    @Schema(description = "수정일시", example = "2024-03-02T15:30:00")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "care_history_id")
    @Schema(description = "연결된 간병 기록 ID", example = "10")
    private CareHistory careHistory;
}
