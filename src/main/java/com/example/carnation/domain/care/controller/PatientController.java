package com.example.carnation.domain.care.controller;

import com.example.carnation.common.response.ApiResponse;
import com.example.carnation.domain.care.dto.PatientRequestDto;
import com.example.carnation.domain.care.service.PatientService;
import com.example.carnation.security.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
