package com.example.carnation.domain.care.helper;

import com.example.carnation.domain.care.entity.CareMatching;
import com.example.carnation.domain.user.common.entity.User;
import com.example.carnation.domain.user.wallet.dto.TransferRequestDto;
import com.example.carnation.domain.user.wallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CareHelper {
    private final WalletService walletService;

    public Boolean isUserPatient(User user) {
        return user.getPatient() == null ? null : user.getPatient().getIsVisible();
    }

    public Boolean isUserCaregiver(User user) {
        return user.getCaregiver() == null ? null : user.getCaregiver().getIsVisible();
    }

    /** 🔹 현재 사용자가 해당 CareMatching의 간병인인지 확인 */
    public boolean isUserCaregiver(User user, CareMatching careMatching) {
        return user.getCaregiver() != null &&
                user.getCaregiver().getUser().getId().equals(careMatching.getCaregiver().getUser().getId());
    }

    /** 🔹 현재 사용자가 해당 CareMatching의 환자인지 확인 */
    public boolean isUserPatient(User user, CareMatching careMatching) {
        return user.getPatient() != null &&
                user.getPatient().getUser().getId().equals(careMatching.getPatient().getUser().getId());
    }

    /** 🔹 매칭이 이루어질 때 결제 처리 */
    public void processMatchingPayment(boolean isCaregiver, User user, CareMatching careMatching) {
        // 돈을 지불하는 사람은 항상 환자 여야함
        // 돈을 지불하는 사람
        User payer = isCaregiver ? careMatching.getPatient().getUser() : user;
        // 돈을 받는 사람
        User payee = isCaregiver ? user : careMatching.getCaregiver().getUser();

        walletService.transfer(payer, TransferRequestDto.of(careMatching, payee));
    }

    /** 🔹 매칭이 취소되거나 종료될 때 상태 초기화 */
    public void resetMatchingStatus(CareMatching careMatching) {
        careMatching.getCaregiver().updateIsMatch(true);
        careMatching.getPatient().updateIsMatch(true);
    }
}
