package com.example.carnation.domain.user.repository;

import com.example.carnation.domain.user.entity.UserWallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserWalletRepository extends JpaRepository<UserWallet, Long> {
}
