package com.example.carnation.testInfo;

import com.example.carnation.domain.careRecord.constans.RelationshipType;
import com.example.carnation.domain.careRecord.dto.CaregiverRequestDto;
import com.example.carnation.domain.careRecord.dto.PatientRequestDto;

import java.time.LocalDate;

public class TestCareRecord {

    public static CaregiverRequestDto getCaregiverRequestDto1() {
        return new CaregiverRequestDto(
                "이영희",
                "900101-2345678",
                170.5,
                65.3,
                "O형",
                RelationshipType.FRIEND,
                "010-1234-5678",
                LocalDate.of(2024, 9, 25),
                LocalDate.of(2025, 10, 25)
        );
    }

    public static CaregiverRequestDto getCaregiverRequestDto2() {
        return new CaregiverRequestDto(
                "박민수",
                "850505-3456789",
                175.0,
                70.2,
                "A형",
                RelationshipType.FRIEND,
                "010-5678-1234",
                LocalDate.of(2023, 6, 10),
                LocalDate.of(2024, 7, 15)
        );
    }

    public static PatientRequestDto getPatientRequestDto1() {
        return new PatientRequestDto(
                "김철수",
                "800101-1234567"
        );
    }

    public static PatientRequestDto getPatientRequestDto2() {
        return new PatientRequestDto(
                "최영수",
                "750302-9876543"
        );
    }
}
