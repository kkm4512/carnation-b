package com.example.carnation.domain.care.controller;

import com.example.carnation.common.dto.PageSearchDto;
import com.example.carnation.common.response.ApiResponse;
import com.example.carnation.domain.care.dto.PatientRequestDto;
import com.example.carnation.domain.care.dto.PatientSimpleResponseDto;
import com.example.carnation.domain.care.service.PatientService;
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
@RequestMapping("/api/v1/patient")
@Tag(name = "Patient API", description = "환자 관련 API")
public class PatientController {
    private final PatientService patientService;

    @PostMapping
    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "환자 정보 등록",
            description = "현재 로그인한 사용자의 환자 정보를 등록합니다."
    )
    public ApiResponse<Void> create(
            @AuthenticationPrincipal AuthUser authUser,
            @Valid @RequestBody PatientRequestDto dto
    ){
        patientService.create(authUser, dto);
        return ApiResponse.of(SUCCESS);
    }

    @GetMapping("/available")
    @Operation(
            summary = "매칭 가능한 환자 목록 조회",
            description = "매칭 가능한 환자 목록들을 페이지네이션으로 조회합니다."
    )
    public ApiResponse<Page<PatientSimpleResponseDto>> findPageByAvailable(
            @ModelAttribute @Valid PageSearchDto pageSearchDto
    ){
        Pageable pageable = PageSearchDto.of(pageSearchDto);
        Page<PatientSimpleResponseDto> response = patientService.findPageByAvailable(pageable);
        return ApiResponse.of(SUCCESS, response);
    }
}
