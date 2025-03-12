package com.example.carnation.common.constans;

import lombok.Getter;

@Getter
public enum SortByEnum {
    CREATED_AT("createdAt"),
    UPDATED_AT("updatedAt")
    ;

    private final String description;

    SortByEnum(String description) {
        this.description = description;
    }
}
