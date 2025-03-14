package com.example.carnation.common.dto.constans;
import lombok.Getter;
import org.springframework.data.domain.Sort;

@Getter
public enum SortEnum {
    // 작은 값 -> 큰 값 (오름차순)
    ASC("오름차순", Sort.Direction.ASC),

    // 큰 값 -> 작은 값 (내림차순)
    DESC("내림차순", Sort.Direction.DESC);

    private final String description;
    private final Sort.Direction direction;

    SortEnum(String description, Sort.Direction direction) {
        this.description = description;
        this.direction = direction;
    }

    @Override
    public String toString() {
        return description;
    }
}
