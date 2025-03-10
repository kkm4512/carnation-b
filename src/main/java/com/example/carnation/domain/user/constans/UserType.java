package com.example.carnation.domain.user.constans;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "사용자 역할 Enum") // Swagger 문서화
public enum UserType {
    CAREGIVER("간병인"),

    PATIENT("피간병인");

    private final String description;
}
