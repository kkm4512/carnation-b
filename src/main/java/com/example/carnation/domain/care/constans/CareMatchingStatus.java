package com.example.carnation.domain.care.constans;

import lombok.Getter;

@Getter
public enum CareMatchingStatus {
    PENDING,      // 대기 중 (매칭 요청을 보낸 상태)
    MATCHING,  // 간병 진행 중 (간병이 시작된 상태)
    CANCEL,     // 매칭 취소 (간병인 또는 환자가 취소함)
    END;          // 매칭 종료 (간병 완료 후 종료)
}
