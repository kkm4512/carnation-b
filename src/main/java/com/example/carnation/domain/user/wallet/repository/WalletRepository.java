package com.example.carnation.domain.user.wallet.repository;

import com.example.carnation.domain.user.wallet.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
}
