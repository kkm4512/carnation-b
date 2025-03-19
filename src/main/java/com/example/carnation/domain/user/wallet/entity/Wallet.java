package com.example.carnation.domain.user.wallet.entity;

import com.example.carnation.common.exception.PaymentException;
import com.example.carnation.common.response.enums.PaymentApiResponseEnum;
import com.example.carnation.domain.user.wallet.constans.BankType;
import com.example.carnation.domain.user.common.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    @Schema(description = "사용자와 연결된 사용자 ID", example = "5")
    private User user;

    /** 사용자의 은행 */
    @Schema(description = "사용자의 은행", example = "KEB_HANA")
    @Column
    @Enumerated(EnumType.STRING)
    private BankType bank;

    /** 사용자의 계좌번호 */
    @Schema(description = "사용자의 계좌번호", example = "123-4567-8910")
    @Column(unique = true)
    private String accountNumber;

    /** 사용자의 계좌 잔액 */
    @Column(nullable = false)
    private Integer balance;

    // 일반 회원가입
    public Wallet(User user, BankType bank, String accountNumber) {
        this.user = user;
        this.bank = bank;
        this.accountNumber = accountNumber;
        this.balance = 0;  // 기본 잔액 0원
    }

    // 소셜 회원가입
    public Wallet(User user) {
        this.user = user;
        this.balance = 0;  // 기본 잔액 0원
    }

    public static Wallet of(User user, BankType bank, String accountNumber) {
        return new Wallet(user, bank, accountNumber);
    }

    public static Wallet of(User user) {
        return new Wallet(user);
    }

    /** 💰 금액 입금 (충전) */
    public void deposit(int amount) {
        if (amount <= 0) {
            throw new PaymentException(PaymentApiResponseEnum.INVALID_PAYMENT_AMOUNT);
        }
        this.balance += amount;
    }

    /** 💸 금액 출금 (결제) */
    public void withdraw(int amount) {
        if (amount <= 0) {
            throw new PaymentException(PaymentApiResponseEnum.INVALID_PAYMENT_AMOUNT);
        }
        if (this.balance < amount) {
            throw new PaymentException(PaymentApiResponseEnum.INSUFFICIENT_BALANCE);
        }
        this.balance -= amount;
    }
}
