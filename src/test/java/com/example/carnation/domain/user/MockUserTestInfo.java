package com.example.carnation.domain.user;

import com.example.carnation.domain.user.common.constans.AuthProvider;
import com.example.carnation.domain.user.wallet.constans.BankType;
import com.example.carnation.domain.user.common.entity.User;
import com.example.carnation.security.AuthUser;
import com.example.carnation.security.UserRole;

import java.time.LocalDateTime;

public class MockUserTestInfo {

    // 공통된 테스트 데이터 상수 선언
    private static final Long ID_1 = 1L;
    private static final Long ID_2 = 2L;
    private static final String EMAIL_1 = "test@naver.com1";
    private static final String EMAIL_2 = "test@naver.com2";
    private static final String NICKNAME_1 = "testNickname1";
    private static final String NICKNAME_2 = "testNickname2";
    private static final String PASSWORD_1 = "!@Skdud3401";
    private static final String PASSWORD_2 = "!@Skdud3402";
    private static final String PHONE_1 = "01011111111";
    private static final String PHONE_2 = "01022222222";
    private static final String SSN_1 = "111111-1111111";
    private static final String SSN_2 = "222222-2222222";
    private static final String ACCOUNT_NUMBER1 = "111-1111-1111";
    private static final String ACCOUNT_NUMBER2 = "222-2222-2222";
    private static final UserRole ROLE = UserRole.ROLE_USER;
    private static final AuthProvider PROVIDER = AuthProvider.GENERAL;
    private static final LocalDateTime NOW = LocalDateTime.now();
    private static final BankType BANK = BankType.KB;


    public static AuthUser getAuthUser1() {
        return new AuthUser(ID_1, EMAIL_1, NICKNAME_1, ROLE, PROVIDER);
    }

    public static AuthUser getAuthUser2() {
        return new AuthUser(ID_2, EMAIL_2, NICKNAME_2, ROLE, PROVIDER);
    }

    // 1. User를 먼저 생성 (Patient와 Caregiver는 나중에 설정)
    public static User getUser1() {
        return new User(ID_1, NICKNAME_1, EMAIL_1, PHONE_1, PASSWORD_1, ROLE, SSN_1, PROVIDER, NOW, NOW, null, null, BANK, ACCOUNT_NUMBER1);
    }

    public static User getUser2() {
        return new User(ID_2, NICKNAME_2, EMAIL_2, PHONE_2, PASSWORD_2, ROLE, SSN_2, PROVIDER, NOW, NOW, null, null, BANK, ACCOUNT_NUMBER2);
    }

    public static User getUserNoId1(){
        return new User(null, NICKNAME_1, EMAIL_1, PHONE_1, PASSWORD_1, ROLE, SSN_1, PROVIDER, NOW, NOW, null, null, BANK, ACCOUNT_NUMBER1);
    }

    public static User getUserNoId2(){
        return new User(null, NICKNAME_2, EMAIL_2, PHONE_2, PASSWORD_2, ROLE, SSN_2, PROVIDER, NOW, NOW, null, null, BANK, ACCOUNT_NUMBER2);
    }
}
