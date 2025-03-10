package com.example.carnation.domain.care.controller;

import com.example.carnation.common.response.ApiResponse;
import com.example.carnation.domain.care.dto.CareHistoryRequestDto;
import com.example.carnation.domain.care.service.CareHistoryService;
import com.example.carnation.security.AuthUser;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.example.carnation.common.response.enums.BaseApiResponse.SUCCESS;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/v1/careHistory")
@Tag(name = "CareHistory API", description = "간병 기록 관리 API")
public class CareHistoryController {
    private final CareHistoryService careHistoryService;

    @PostMapping(value = "/{careAssignmentId}", consumes = "multipart/form-data")
    @SecurityRequirement(name = "JWT")
    public ApiResponse<Void> create(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long careAssignmentId,
            @Valid @RequestPart CareHistoryRequestDto dto // DTO 전체 유효성 검사 적용
    ) {
        careHistoryService.create(authUser, careAssignmentId, dto);
        return ApiResponse.of(SUCCESS);
    }
}

