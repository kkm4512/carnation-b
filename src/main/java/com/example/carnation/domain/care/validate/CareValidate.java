package com.example.carnation.domain.care.validate;

import com.example.carnation.common.exception.CareException;
import com.example.carnation.common.response.enums.CareApiResponseEnum;
import com.example.carnation.domain.care.entity.Caregiver;
import com.example.carnation.domain.care.entity.Patient;
import com.example.carnation.domain.user.common.entity.User;

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

    // user가 간병인으로서 공고를 올렸는데, 환자로 공고를 올릴려고 할시 예외발생
    public static void validateCaregiverRegisterConflict(User user, Boolean flag) {
        if (user.getPatient() != null &&
                user.getPatient().getIsVisible().equals(Boolean.TRUE) &&
                flag.equals(Boolean.TRUE)) {
            throw new CareException(CareApiResponseEnum.DUPLICATE_JOB_POSTING_AS_PATIENT);
        }
    }

    // user가 환자으로서 공고를 올렸는데, 간병인으로 공고를 올릴려고 할시 예외발생
    public static void validatePatientRegisterConflict(User user, Boolean flag) {
        if (user.getCaregiver() != null &&
                user.getCaregiver().getIsVisible().equals(Boolean.TRUE) &&
                flag.equals(Boolean.TRUE)) {
            throw new CareException(CareApiResponseEnum.DUPLICATE_JOB_POSTING_AS_CAREGIVER);
        }
    }





}
