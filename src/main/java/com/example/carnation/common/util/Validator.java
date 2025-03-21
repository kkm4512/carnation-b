package com.example.carnation.common.util;

import com.example.carnation.common.exception.BaseException;
import com.example.carnation.common.exception.ValidatorException;
import com.example.carnation.common.response.enums.ValidatorApiResponseEnum;

public class Validator {
    public static void validateNotNullAndEqual(Object o1, Object o2, BaseException baseException) {
        nullCheck(o1,o2);
        if (o1.equals(o2)) {
            throw baseException;
        }
    }

    public static void validateNotNullAndNotEqual(Object o1, Object o2, BaseException baseException) {
        nullCheck(o1,o2);
        if (!o1.equals(o2)) {
            throw baseException;
        }
    }

    private static void nullCheck(Object o1, Object o2) {
        if (o1 == null || o2 == null) {
            throw new ValidatorException(ValidatorApiResponseEnum.NULL_VALUE_NOT_ALLOWED);
        }
    }
}
