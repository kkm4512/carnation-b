package com.example.carnation.domain.care.controller;

import com.example.carnation.common.response.ApiResponse;
import com.example.carnation.domain.care.constans.CareMatchingStatus;
import com.example.carnation.domain.care.dto.*;
import com.example.carnation.domain.care.service.CareMatchingService;
import com.example.carnation.security.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.example.carnation.common.response.enums.BaseApiResponseEnum.SUCCESS;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/v1/care-matching")
@Tag(name = "CareMatching API", description = "간병 매칭 관련 API")
public class CareMatchingController {
    private final CareMatchingService careMatchingService;

    @PostMapping("/me")
    @Operation(summary = "간병인,환자의 ID를 통해 간병 매칭 생성")
    @SecurityRequirement(name = "JWT")
    public ApiResponse<Void> generate(
            @AuthenticationPrincipal AuthUser authUser,
            // 요청한 유저의 타입
            @RequestBody @Valid CareMatchingRequestDto dto
    ) {
        careMatchingService.generate(authUser,dto);
        return ApiResponse.of(SUCCESS);
    }

    @Operation(summary = "간병 매칭 상세 조회", description = "특정 간병 매칭의 상세 정보를 조회합니다.")
    @GetMapping("/{careMatchingId}")
    public ApiResponse<CareMatchingDetailResponse> findOne(
            @PathVariable Long careMatchingId
    ) {
        CareMatchingDetailResponse response = careMatchingService.findOne(careMatchingId);
        return ApiResponse.of(SUCCESS, response);
    }

    @Operation(summary = "간병 매칭 상태 변경", description = "JWT 인증된 사용자가 간병 매칭의 상태를 변경합니다.")
    @PutMapping("/{careMatchingId}")
    @SecurityRequirement(name = "JWT")
    public ApiResponse<Void> modifyStatus(
            @AuthenticationPrincipal AuthUser authUser,
            // 요청한 유저의 타입
            @RequestParam(value = "matchStatus") CareMatchingStatus matchStatus,
            @PathVariable Long careMatchingId
    ) {
        CareMatchingStatusUpdateRequestDto dto = CareMatchingStatusUpdateRequestDto.of(matchStatus,careMatchingId);
        careMatchingService.modifyStatus(authUser, dto, careMatchingId);
        return ApiResponse.of(SUCCESS);
    }

    /**
     * 자신이 기록한 간병 기록 페이지네이션 조회
     * 자신의 것을 조회하는 것이기 때문에, 로그인이 되어 있어야함
     */
    @Operation(summary = "로그인한 유저 (간병인)의 간병 매칭 목록 페이지네이션 반환")
    @SecurityRequirement(name = "JWT")
    @GetMapping("/me/caregiver")
    public ApiResponse<Page<CareMatchingSimpleResponse>> findPageByCaregiver(
            @AuthenticationPrincipal AuthUser authUser,
            @ModelAttribute @Valid CareMatchingStatusSearchDto dto
    ) {
        Page<CareMatchingSimpleResponse> response = careMatchingService.findPageByCaregiver(authUser, dto);
        return ApiResponse.of(SUCCESS, response);
    }

    @Operation(summary = "로그인한 유저 (환자)의 간병 매칭 목록 페이지네이션 반환")
    @SecurityRequirement(name = "JWT")
    @GetMapping("/me/patient")
    public ApiResponse<Page<CareMatchingSimpleResponse>> findPageByPatient(
            @AuthenticationPrincipal AuthUser authUser,
            @ModelAttribute @Valid CareMatchingStatusSearchDto dto
    ) {
        Page<CareMatchingSimpleResponse> response = careMatchingService.findPageByPatient(authUser, dto);
        return ApiResponse.of(SUCCESS, response);
    }

}
