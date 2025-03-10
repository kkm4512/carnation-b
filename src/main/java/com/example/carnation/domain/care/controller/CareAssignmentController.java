package com.example.carnation.domain.care.controller;

import com.example.carnation.common.response.ApiResponse;
import com.example.carnation.domain.care.dto.CareAssignmentResponse;
import com.example.carnation.domain.care.dto.CaregiverRequestDto;
import com.example.carnation.domain.care.dto.PatientRequestDto;
import com.example.carnation.domain.care.service.CareAssignmentService;
import com.example.carnation.security.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.example.carnation.common.response.enums.BaseApiResponse.SUCCESS;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/v1/CareAssignment")
@Tag(name = "CareAssignment API", description = "간병 배정 관리 API")
public class CareAssignmentController {
    private final CareAssignmentService careRecordService;

    @Operation(
            summary = "간병 배정 등록",
            description = "피간병인과 간병인의 정보를 입력하여 간병 배정을 등록합니다."
    )
    @PostMapping
    @SecurityRequirement(name = "JWT")
    public ApiResponse<Void> create(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestBody @Valid CaregiverRequestDto caregiverDto,
            @RequestBody @Valid PatientRequestDto patientDto
    ) {
        careRecordService.create(authUser, caregiverDto, patientDto);
        return ApiResponse.of(SUCCESS);
    }

    /**
     * 자신이 기록한 간병 기록 모두 조회
     * 자신의 것을 조회하는 것이기 때문에, 로그인이 되어 있어야함
     */
    @Operation(
            summary = "내 간병 배정 기록 조회",
            description = "현재 로그인한 사용자의 간병 배정 기록을 페이지네이션하여 조회합니다."
    )
    @SecurityRequirement(name = "JWT")
    @GetMapping
    public ApiResponse<Page<CareAssignmentResponse>> readAllMe(
            @AuthenticationPrincipal AuthUser authUser,
            @Parameter(description = "페이지 번호 (기본값: 1)", example = "1")
            @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "페이지 크기 (기본값: 10)", example = "10")
            @RequestParam(defaultValue = "10") Integer size
    ) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.Direction.DESC, "createdAt");
        Page<CareAssignmentResponse> response = careRecordService.readAllMe(authUser, pageable);
        return ApiResponse.of(SUCCESS, response);
    }
}
