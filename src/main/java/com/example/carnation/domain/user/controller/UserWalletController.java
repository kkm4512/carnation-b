package com.example.carnation.domain.user.controller;

import com.example.carnation.common.response.ApiResponse;
import com.example.carnation.domain.user.dto.UserDepositRequestDto;
import com.example.carnation.domain.user.dto.UserTransferRequestDto;
import com.example.carnation.domain.user.dto.UserWithdrawRequestDto;
import com.example.carnation.domain.user.service.UserWalletService;
import com.example.carnation.security.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.example.carnation.common.response.enums.BaseApiResponseEnum.SUCCESS;

@RestController
@RequestMapping("/api/v1/user/wallet")
@RequiredArgsConstructor
@Tag(name = "UserWallet API", description = "카네이션 시스템 내부 사용자 결제 관련 API")
public class UserWalletController {

    private final UserWalletService userWalletService;

    /**
     * 송금 요청 API
     */
    @PostMapping("/transfer")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "카네이션 내부 (카네이션 가상 계좌) 자신 -> 카네이션 내부 (카네이션 가상 계좌) 타인에게 송금", description = "로그인한 사용자가 다른 사용자에게 금액을 송금하는 API")
    public ApiResponse<Void> transfer(
            @AuthenticationPrincipal AuthUser authUser,
            @Valid @RequestBody UserTransferRequestDto dto
    ) {
        userWalletService.transfer(authUser,dto);
        return ApiResponse.of(SUCCESS);
    }

    /**
     * 잔액 조회 API
     */
    @GetMapping("/balance")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "카네이션 내부 자신의 잔액 조회")
    public ApiResponse<Integer> getBalance(
            @AuthenticationPrincipal AuthUser authUser
    ) {
        int balance = userWalletService.getBalance(authUser);
        return ApiResponse.of(SUCCESS,balance);
    }

    /**
     * 🔹 잔액 입금 API
     */
    @PostMapping("/addBalance")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "카네이션 외부 (사용자의 실제계좌) -> 카네이션 내부 (카네이션 가상계좌)")
    public ApiResponse<Void> addBalance(
            @AuthenticationPrincipal AuthUser authUser,
            @Valid @RequestBody UserDepositRequestDto dto
    ) {
        userWalletService.addBalance(authUser, dto);
        return ApiResponse.of(SUCCESS);
    }

    /**
     * 🔹 잔액 출금 API
     */
    @PostMapping("/minusBalance")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "카네이션 내부 (카네이션 가상계좌) -> 카네이션 외부 (사용자의 실제계좌)")
    public ApiResponse<Void> minusBalance(
            @AuthenticationPrincipal AuthUser authUser,
            @Valid @RequestBody UserWithdrawRequestDto dto
    ) {
        userWalletService.minusBalance(authUser, dto);
        return ApiResponse.of(SUCCESS);
    }
}
