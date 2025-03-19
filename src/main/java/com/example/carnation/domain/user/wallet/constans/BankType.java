package com.example.carnation.domain.user.wallet.constans;

import lombok.Getter;

@Getter
public enum BankType {
    KEB_HANA("KEB 하나은행"),
    SHINHAN("신한은행"),
    KB("국민은행"),
    WOORI("우리은행"),
    NH("농협은행"),
    TOSS("토스뱅크"),
    KAKAO("카카오뱅크");

    private final String description;

    BankType(String description) {
        this.description = description;
    }
}
