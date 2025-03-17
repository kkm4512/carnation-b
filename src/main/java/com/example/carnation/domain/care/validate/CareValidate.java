package com.example.carnation.domain.care.validate;

import com.example.carnation.common.exception.CareException;
import com.example.carnation.common.response.enums.CareApiResponseEnum;
import com.example.carnation.domain.care.entity.Caregiver;
import com.example.carnation.domain.care.entity.Patient;

public class CareValidate {
    public static void validateCareMatching(Boolean flag) {
        if (flag) {
            throw new CareException(CareApiResponseEnum.CAREGIVER_PATIENT_CONFLICT);
        }
    }

    /**
     * 사용자가 자신의 간병인 계정을 자신의 환자로 매칭하려고 하면 예외를 발생시킵니다.
     *
     * @param patient   환자 객체
     * @param caregiver 간병인 객체
     * @throws CareException 사용자가 자신의 간병인 계정을 자신의 환자로 매칭할 수 없음
     */
    public static void validateSelfCareMatching(Patient patient, Caregiver caregiver) {
        if (patient.getUser().getId().equals(caregiver.getUser().getId())) {
            throw new CareException(CareApiResponseEnum.SELF_CARE_MATCHING_NOT_ALLOWED);
        }
    }



}
