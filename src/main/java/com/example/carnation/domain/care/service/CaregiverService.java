package com.example.carnation.domain.care.service;

import com.example.carnation.domain.care.cqrs.CaregiverQuery;
import com.example.carnation.domain.care.dto.CaregiverRequestDto;
import com.example.carnation.domain.care.entity.Caregiver;
import com.example.carnation.domain.user.cqrs.UserCommand;
import com.example.carnation.domain.user.cqrs.UserQuery;
import com.example.carnation.domain.user.dto.UserResponseDto;
import com.example.carnation.domain.user.entity.User;
import com.example.carnation.security.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        User saveUser = userCommand.create(user);
        return UserResponseDto.of(saveUser);
    }
}
