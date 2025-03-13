package com.example.carnation.domain.care.controller;

import com.example.carnation.common.dto.PageSearchDto;
import com.example.carnation.common.response.ApiResponse;
import com.example.carnation.domain.care.dto.CareAssignmentResponse;
import com.example.carnation.domain.care.dto.CaregiverPatientRequestDto;
import com.example.carnation.domain.care.service.CareAssignmentService;
import com.example.carnation.security.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.example.carnation.common.response.enums.BaseApiResponse.SUCCESS;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/v1/careAssignment")
@Tag(name = "CareAssignment API", description = "간병 배정 API")
public class CareAssignmentController {
    private final CareAssignmentService careAssignmentService;

    @Operation(
            summary = "간병 배정 등록",
            description = "피간병인과 간병인의 정보를 입력하여 간병 배정을 등록합니다."
    )
    @PostMapping
    @SecurityRequirement(name = "JWT")
    public ApiResponse<Void> create(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestBody @Valid CaregiverPatientRequestDto dto
    ) {
        careAssignmentService.create(authUser, dto.getCaregiverDto(), dto.getPatientDto());
        return ApiResponse.of(SUCCESS);
    }

    /**
     * 자신이 기록한 간병 기록 모두 조회
     * 자신의 것을 조회하는 것이기 때문에, 로그인이 되어 있어야함
     */
    @Operation(
            summary = "내 간병 배정 기록들 조회",
            description = "현재 로그인한 사용자의 간병 배정 기록을 페이지네이션하여 조회합니다."
    )
    @SecurityRequirement(name = "JWT")
    @GetMapping
    public ApiResponse<Page<CareAssignmentResponse>> readAllMePage(
            @AuthenticationPrincipal AuthUser authUser,
            @Parameter(description = "페이징 처리에 필요한 page(페이지 번호), size(페이지 크기), sort(정렬 방식), sortBy(정렬 기준 필드)")
            @ModelAttribute @Valid PageSearchDto pageSearchDto
    ) {
        Pageable pageable = PageSearchDto.of(pageSearchDto);
        Page<CareAssignmentResponse> response = careAssignmentService.readAllMePage(authUser, pageable);
        return ApiResponse.of(SUCCESS, response);
    }
}
