package com.example.carnation.domain.care.service;

import com.example.carnation.common.dto.PageSearchDto;
import com.example.carnation.domain.care.cqrs.CaregiverQuery;
import com.example.carnation.domain.care.dto.CaregiverIsMatchSearchDto;
import com.example.carnation.domain.care.dto.CaregiverRequestDto;
import com.example.carnation.domain.care.dto.CaregiverSimpleResponseDto;
import com.example.carnation.domain.care.entity.Caregiver;
import com.example.carnation.domain.care.validate.CareValidate;
import com.example.carnation.domain.user.auth.dto.UserResponseDto;
import com.example.carnation.domain.user.common.cqrs.UserCommand;
import com.example.carnation.domain.user.common.cqrs.UserQuery;
import com.example.carnation.domain.user.common.entity.User;
import com.example.carnation.security.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j(topic = "CaregiverService")
@RequiredArgsConstructor
public class CaregiverService {
    private final UserQuery userQuery;
    private final UserCommand userCommand;
    private final CaregiverQuery caregiverQuery;


    // 간병인 정보 저장
    @Transactional
    public UserResponseDto generate(final AuthUser authUser, final CaregiverRequestDto dto) {
        User user = userQuery.readById(authUser.getUserId());
        Caregiver caregiver = Caregiver.of(user, dto);
        user.updateCareGiver(caregiver);
        CareValidate.validateCaregiverRegisterConflict(user,dto.getIsVisible());
        User saveUser = userCommand.create(user);
        return UserResponseDto.of(saveUser);
    }

    public Page<CaregiverSimpleResponseDto> findPageByAvailable(CaregiverIsMatchSearchDto dto) {
        Pageable pageable = PageSearchDto.of(dto);
        Page<Caregiver> result = caregiverQuery.readPageByAvailable(dto,pageable);
        return result.map(CaregiverSimpleResponseDto::of);
    }

    @Transactional
    public void modifyIsVisible(AuthUser authUser, Boolean IsVisible) {
        User user = userQuery.readById(authUser.getUserId());
        Caregiver caregiver = user.getCaregiver();
        user.validateIsNotMe(caregiver.getUser());
        CareValidate.validateCaregiverRegisterConflict(user,IsVisible);
        caregiver.updateIsVisible(IsVisible);
    }
}
