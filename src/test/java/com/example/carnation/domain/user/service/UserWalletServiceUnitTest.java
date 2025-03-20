package com.example.carnation.domain.user.service;

import com.example.carnation.domain.user.MockUserInfo;
import com.example.carnation.domain.user.common.cqrs.UserQuery;
import com.example.carnation.domain.user.common.entity.User;
import com.example.carnation.domain.user.wallet.dto.DepositRequestDto;
import com.example.carnation.domain.user.wallet.dto.TransferRequestDto;
import com.example.carnation.domain.user.wallet.dto.WithdrawRequestDto;
import com.example.carnation.domain.user.wallet.service.WalletService;
import com.example.carnation.security.AuthUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserWalletServiceUnitTest {

    @Mock
    private UserQuery userQuery;

    @InjectMocks
    private WalletService userWalletService;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = MockUserInfo.getUser1();
        user2 = MockUserInfo.getUser2();
        user1.getUserWallet().deposit(10000);
        user2.getUserWallet().deposit(10000);
    }

    @Test
    @DisplayName("카네이션 내부 (사용자 계좌) -> 카네이션 내부 (다른 사용자 계좌) / 송금")
    void test1() {
        AuthUser authUser = MockUserInfo.getAuthUser1();
        TransferRequestDto dto = new TransferRequestDto(3000, user2.getId());

        when(userQuery.readById(authUser.getUserId())).thenReturn(user1);
        when(userQuery.readById(dto.getTargetId())).thenReturn(user2);

        userWalletService.transfer(MockUserInfo.getUser1(), dto);

        assertEquals(7000, user1.getUserWallet().getBalance());
        assertEquals(13000, user2.getUserWallet().getBalance());
    }

    @Test
    @DisplayName("카네이션 내부 (사용자 계좌) / 잔액조회")
    void test2() {
        AuthUser authUser = MockUserInfo.getAuthUser1();

        when(userQuery.readById(authUser.getUserId())).thenReturn(user1);

        int balance = userWalletService.getBalance(authUser);

        assertEquals(10000, balance);
    }

    @Test
    @DisplayName("카네이션 외부 (실제 사용자 계좌) -> 카네이션 내부 (사용자 계좌) / 입금")
    void test3() {
        AuthUser authUser = MockUserInfo.getAuthUser1();
        DepositRequestDto dto = new DepositRequestDto(5000);

        when(userQuery.readById(authUser.getUserId())).thenReturn(user1);

        userWalletService.addBalance(authUser, dto);

        assertEquals(15000, user1.getUserWallet().getBalance());
    }

    @Test
    @DisplayName("카네이션 내부 (사용자 계좌) -> 카네이션 외부 (실제 사용자 계좌) / 출금")
    void test4() {
        AuthUser authUser = MockUserInfo.getAuthUser1();
        WithdrawRequestDto dto = new WithdrawRequestDto(4000);

        when(userQuery.readById(authUser.getUserId())).thenReturn(user1);

        userWalletService.minusBalance(authUser, dto);

        assertEquals(6000, user1.getUserWallet().getBalance());
    }
}