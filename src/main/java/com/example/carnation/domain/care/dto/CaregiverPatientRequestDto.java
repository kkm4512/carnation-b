package com.example.carnation.domain.care.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "피간병인, 간병인 요청 DTO") // DTO 전
public class CaregiverPatientRequestDto {
    private CaregiverRequestDto caregiverDto;
    private PatientRequestDto patientDto;
}
