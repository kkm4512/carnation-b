package com.example.carnation.domain.care.service;

import com.example.carnation.common.dto.PageSearchDto;
import com.example.carnation.common.exception.CareException;
import com.example.carnation.common.response.enums.CareApiResponseEnum;
import com.example.carnation.domain.care.cqrs.CareMatchingCommand;
import com.example.carnation.domain.care.cqrs.CareMatchingQuery;
import com.example.carnation.domain.care.cqrs.CaregiverQuery;
import com.example.carnation.domain.care.cqrs.PatientQuery;
import com.example.carnation.domain.care.dto.*;
import com.example.carnation.domain.care.entity.CareMatching;
import com.example.carnation.domain.care.entity.Caregiver;
import com.example.carnation.domain.care.entity.Patient;
import com.example.carnation.domain.care.helper.CareHelper;
import com.example.carnation.domain.care.validate.CareValidate;
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
@RequiredArgsConstructor
@Slf4j
public class CareMatchingService {
    private final CareMatchingQuery careMatchingQuery;
    private final CareMatchingCommand careMatchingCommand;
    private final CaregiverQuery caregiverQuery;
    private final PatientQuery patientQuery;
    private final UserQuery userQuery;
    private final CareHelper careHelper;

    // 간병인 - 환자 생성 비즈니스 로직
    @Transactional
    public CareMatchingDetailResponse generate(final AuthUser authUser, final CareMatchingRequestDto dto) {
        User user = userQuery.readById(authUser.getUserId());
        Boolean isPatient = careHelper.isUserPatient(user);
        Patient patient;
        Caregiver caregiver;
        // 요청을 보낸 사람이 환자라면, target은 간병인
        if (isPatient) {
            patient = user.getPatient();
            caregiver = caregiverQuery.readById(dto.getTargetId());
        }
        // 요청을 보낸 사람이 간병인이라면, target은 환자
        else {
            caregiver = user.getCaregiver();
            patient = patientQuery.readById(dto.getTargetId());
        }
        CareValidate.validateSelfCareMatching(patient, caregiver);
        Boolean isCareMatching = careMatchingQuery.existsByActiveUserInCareMatching(patient, caregiver);
        CareValidate.validateCareMatching(isCareMatching);
        CareMatching careMatching = CareMatching.of(patient, caregiver, dto);
        CareMatching saveCareMatching = careMatchingCommand.create(careMatching);
        // 현재 환자와 간병인은 매칭 불가 상태
        saveCareMatching.getCaregiver().updateIsMatch(false);
        saveCareMatching.getPatient().updateIsMatch(false);
        return CareMatchingDetailResponse.of(saveCareMatching);
    }


    // 로그인해 있는 유저의 간병인으로 매칭되있는것들을 가져옴
    public Page<CareMatchingSimpleResponse> findPageMeByCaregiver(final AuthUser authUser, final CareMatchingStatusSearchDto dto) {
        Pageable pageable = PageSearchDto.of(dto);
        User user = userQuery.readById(authUser.getUserId());
        Page<CareMatching> responses = careMatchingQuery.readPageMeByCaregiver(user.getCaregiver(),pageable,dto);
        return responses.map(CareMatchingSimpleResponse::of);
    }

    // 로그인해 있는 유저의 환자로 매칭되있는것들을 가져옴
    public Page<CareMatchingSimpleResponse> findPageMeByPatient(final AuthUser authUser, final CareMatchingStatusSearchDto dto) {
        Pageable pageable = PageSearchDto.of(dto);
        User user = userQuery.readById(authUser.getUserId());
        Page<CareMatching> responses = careMatchingQuery.readPageMeByPatient(user.getPatient(),pageable, dto);
        return responses.map(CareMatchingSimpleResponse::of);
    }

    public CareMatchingDetailResponse findOne(final Long careMatchingId) {
        CareMatching careMatching = careMatchingQuery.readById(careMatchingId);
        return CareMatchingDetailResponse.of(careMatching);
    }

    @Transactional
    public void modifyStatus(final AuthUser authUser, final CareMatchingStatusUpdateRequestDto dto, final Long careMatchingId) {
        User user = userQuery.readById(authUser.getUserId());
        CareMatching careMatching = careMatchingQuery.readById(careMatchingId);

        // 요청한 유저가 환자인지, 간병인인지 구분
        boolean isCaregiver = careHelper.isUserCaregiver(user, careMatching);
        boolean isPatient = careHelper.isUserPatient(user, careMatching);

        // 어느 유저가 들어오던, CareMathcing을 생성한 본인이 맞다면
        if (isCaregiver || isPatient) {
            careMatching.updateStatus(dto.getMatchStatus());

            switch (dto.getMatchStatus()) {
                case MATCHING -> careHelper.processMatchingPayment(isCaregiver, user, careMatching);
                case CANCEL, END -> careHelper.resetMatchingStatus(careMatching);
            }
        }
        else {
            throw new CareException(CareApiResponseEnum.USER_NOT_AUTHORIZED_FOR_CARE_MATCHING);
        }
    }



}
