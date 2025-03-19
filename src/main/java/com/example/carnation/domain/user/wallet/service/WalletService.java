package com.example.carnation.domain.user.wallet.service;

import com.example.carnation.domain.user.common.cqrs.UserQuery;
import com.example.carnation.domain.user.common.entity.User;
import com.example.carnation.domain.user.common.validate.UserValidate;
import com.example.carnation.domain.user.wallet.dto.DepositRequestDto;
import com.example.carnation.domain.user.wallet.dto.TransferRequestDto;
import com.example.carnation.domain.user.wallet.dto.WithdrawRequestDto;
import com.example.carnation.domain.user.wallet.entity.Wallet;
import com.example.carnation.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WalletService {
    private final UserQuery userQuery;

    @Transactional
    public void transfer(final AuthUser authUser, final TransferRequestDto dto) {
        UserValidate.validateSelfTransferNotAllowed(authUser.getUserId(),dto.getTargetId());
        User user = userQuery.readById(authUser.getUserId());
        Wallet userWallet1 = user.getUserWallet();
        userWallet1.withdraw(dto.getAmount());

        User user2 = userQuery.readById(dto.getTargetId());
        Wallet userWallet2 = user2.getUserWallet();
        userWallet2.deposit(dto.getAmount());
    }

    public Integer getBalance(final AuthUser authUser) {
        User user = userQuery.readById(authUser.getUserId());
        return user.getUserWallet().getBalance();
    }

    /** 🔹 사용자 잔액 충전(입금) */
    @Transactional
    public void addBalance(final AuthUser authUser, final DepositRequestDto dto) {
        User user = userQuery.readById(authUser.getUserId());
        Wallet userWallet = user.getUserWallet();
        userWallet.deposit(dto.getAmount());
    }

    @Transactional
    public void minusBalance(final AuthUser authUser, final WithdrawRequestDto dto) {
        User user = userQuery.readById(authUser.getUserId());
        Wallet userWallet = user.getUserWallet();
        userWallet.withdraw(dto.getAmount());
    }
}
