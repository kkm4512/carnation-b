package com.example.carnation.domain.care.constans;

import lombok.Getter;

@Getter
public enum CareMatchingStatus {
    MATCHED, // 매칭 완료 (간병인과 환자가 서로 수락하여 1:1 매칭됨)
    END;     // 매칭 종료 (간병인 또는 환자가 매칭을 종료함)
}
