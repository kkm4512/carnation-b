package com.example.carnation.domain.care.service;

import com.example.carnation.domain.care.cqrs.CaregiverQuery;
import com.example.carnation.domain.care.dto.CaregiverRequestDto;
import com.example.carnation.domain.care.dto.CaregiverSimpleResponseDto;
import com.example.carnation.domain.care.entity.Caregiver;
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

@Service
@Slf4j(topic = "CaregiverService")
@RequiredArgsConstructor
public class CaregiverService {
    private final UserQuery userQuery;
    private final UserCommand userCommand;
    private final CaregiverQuery caregiverQuery;


    // 간병인 정보 저장
    public UserResponseDto generate(final AuthUser authUser, final CaregiverRequestDto dto) {
        User user = userQuery.readById(authUser.getUserId());
        Caregiver caregiver = Caregiver.of(user, dto);
        user.updateCareGiver(caregiver);
        User saveUser = userCommand.create(user);
        return UserResponseDto.of(saveUser);
    }

    public Page<CaregiverSimpleResponseDto> findPageByAvailable(final Pageable pageable) {
        Page<Caregiver> result = caregiverQuery.readPageByAvailable(pageable);
        return result.map(CaregiverSimpleResponseDto::of);
    }
}
