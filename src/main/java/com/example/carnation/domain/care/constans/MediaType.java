package com.example.carnation.domain.care.constans;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "S3에 저장되는 미디어 개체의 타입") // Swagger 문서화
public enum MediaType {
    IMAGE("사진"),
    VIDEO("동영상");

    private final String mediaType;
}