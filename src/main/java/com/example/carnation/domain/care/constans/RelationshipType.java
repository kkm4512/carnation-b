package com.example.carnation.domain.care.constans;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "피간병인과의 관계 Enum") // Swagger 문서화
public enum RelationshipType {
    // 가족 관계
    SON("아들"),
    DAUGHTER("딸"),
    GRANDSON("손자"),
    GRANDDAUGHTER("손녀"),
    SPOUSE("배우자"),
    PARENT("부모"),
    SIBLING("형제자매"),
    COUSIN("사촌"),
    UNCLE_AUNT("삼촌/이모/고모/숙모"),

    // 친척 및 지인 관계
    FRIEND("친구"),
    NEIGHBOR("이웃"),
    CAREGIVER("전문 간병인"),

    // 기타
    OTHER("기타");

    private final String description;
}
