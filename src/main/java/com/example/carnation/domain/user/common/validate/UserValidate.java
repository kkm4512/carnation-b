package com.example.carnation.domain.user.common.validate;

import com.example.carnation.common.exception.UserException;
import com.example.carnation.common.response.enums.UserApiResponseEnum;

public class UserValidate {
    public static void validateSelfTransferNotAllowed(Long userId1, Long userId2) {
        if (userId1.equals(userId2)) {
            throw new UserException(UserApiResponseEnum.SELF_TRANSFER_NOT_ALLOWED);
        }
    }

}
