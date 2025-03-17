package com.example.carnation.domain.care.controller;

import com.example.carnation.common.dto.PageSearchDto;
import com.example.carnation.common.response.ApiResponse;
import com.example.carnation.domain.care.constans.UserType;
import com.example.carnation.domain.care.dto.CareMatchingRequestDto;
import com.example.carnation.domain.care.dto.CareMatchingResponse;
import com.example.carnation.domain.care.service.CareMatchingService;
import com.example.carnation.security.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.example.carnation.common.response.enums.BaseApiResponseEnum.SUCCESS;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/v1/care-matching")
@Tag(name = "CareMatching API", description = "환자 - 간병인 매칭 API")
public class CareMatchingController {
    private final CareMatchingService careMatchingService;

    @PostMapping
    @Operation(
            summary = "간병인,환자의 ID를 통해 간병 매칭 생성"
    )
    @SecurityRequirement(name = "JWT")
    public ApiResponse<Void> generate(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestParam(value = "userType") UserType userType,
            @RequestBody @Valid CareMatchingRequestDto dto
    ) {
        dto.updateUserType(userType);
        careMatchingService.generate(authUser,dto);
        return ApiResponse.of(SUCCESS);
    }

    /**
     * 자신이 기록한 간병 기록 페이지네이션 조회
     * 자신의 것을 조회하는 것이기 때문에, 로그인이 되어 있어야함
     */
    @Operation(
            summary = "로그인한 유저 (간병인)의 간병 매칭 목록 페이지네이션 반환")
    @SecurityRequirement(name = "JWT")
    @GetMapping("/caregiver")
    public ApiResponse<Page<CareMatchingResponse>> findPageByCaregiver(
            @AuthenticationPrincipal AuthUser authUser,
            @ModelAttribute @Valid PageSearchDto pageSearchDto
    ) {
        Pageable pageable = PageSearchDto.of(pageSearchDto);
        Page<CareMatchingResponse> response = careMatchingService.findPageByCaregiver(authUser, pageable);
        return ApiResponse.of(SUCCESS, response);
    }

    @Operation(
            summary = "로그인한 유저 (환자)의 간병 매칭 목록 페이지네이션 반환")
    @SecurityRequirement(name = "JWT")
    @GetMapping("/patient")
    public ApiResponse<Page<CareMatchingResponse>> findPageByPatient(
            @AuthenticationPrincipal AuthUser authUser,
            @ModelAttribute @Valid PageSearchDto pageSearchDto
    ) {
        Pageable pageable = PageSearchDto.of(pageSearchDto);
        Page<CareMatchingResponse> response = careMatchingService.findPageByPatient(authUser, pageable);
        return ApiResponse.of(SUCCESS, response);
    }
}
