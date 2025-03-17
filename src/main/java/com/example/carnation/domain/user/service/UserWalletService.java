package com.example.carnation.domain.user.service;

import com.example.carnation.domain.user.cqrs.UserQuery;
import com.example.carnation.domain.user.dto.UserDepositRequestDto;
import com.example.carnation.domain.user.dto.UserTransferRequestDto;
import com.example.carnation.domain.user.dto.UserWithdrawRequestDto;
import com.example.carnation.domain.user.entity.User;
import com.example.carnation.domain.user.entity.UserWallet;
import com.example.carnation.domain.user.validate.UserValidate;
import com.example.carnation.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserWalletService {
    private final UserQuery userQuery;

    @Transactional
    public void transfer(final AuthUser authUser, final UserTransferRequestDto dto) {
        UserValidate.validateSelfTransferNotAllowed(authUser.getUserId(),dto.getTargetId());
        User user = userQuery.readById(authUser.getUserId());
        UserWallet userWallet1 = user.getUserWallet();
        userWallet1.withdraw(dto.getAmount());

        User user2 = userQuery.readById(dto.getTargetId());
        UserWallet userWallet2 = user2.getUserWallet();
        userWallet2.deposit(dto.getAmount());
    }

    @Transactional(readOnly = true)
    public Integer getBalance(final AuthUser authUser) {
        User user = userQuery.readById(authUser.getUserId());
        return user.getUserWallet().getBalance();
    }

    /** 🔹 사용자 잔액 충전(입금) */
    @Transactional
    public void addBalance(final AuthUser authUser, final UserDepositRequestDto dto) {
        User user = userQuery.readById(authUser.getUserId());
        UserWallet userWallet = user.getUserWallet();
        userWallet.deposit(dto.getAmount());
    }

    @Transactional
    public void minusBalance(final AuthUser authUser, final UserWithdrawRequestDto dto) {
        User user = userQuery.readById(authUser.getUserId());
        UserWallet userWallet = user.getUserWallet();
        userWallet.withdraw(dto.getAmount());
    }
}
