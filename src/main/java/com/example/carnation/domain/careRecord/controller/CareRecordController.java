package com.example.carnation.domain.careRecord.controller;

import com.example.carnation.common.response.ApiResponse;
import com.example.carnation.domain.careRecord.dto.CaregiverRequestDto;
import com.example.carnation.domain.careRecord.dto.PatientRequestDto;
import com.example.carnation.domain.careRecord.service.CareRecordService;
import com.example.carnation.security.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.example.carnation.common.response.enums.BaseApiResponse.*;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/v1/careRecord")
@Tag(name = "CareRecord API", description = "간병 기록 관리 API")
public class CareRecordController {
    private final CareRecordService careRecordService;

    @Operation(
            summary = "간병 기록 등록",
            description = "피간병인과 간병인의 정보를 입력하여 간병 기록을 등록합니다."
    )
    @PostMapping
    @SecurityRequirement(name = "JWT")
    public ApiResponse<Void> registerCareRecord(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestBody @Valid CaregiverRequestDto caregiverDto,
            @RequestBody @Valid PatientRequestDto patientDto
    ) {
        careRecordService.create(authUser, caregiverDto, patientDto);
        return ApiResponse.of(SUCCESS);
    }
}
