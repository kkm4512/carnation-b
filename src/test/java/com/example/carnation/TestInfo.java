package com.example.carnation;

import com.example.carnation.domain.care.constans.RelationshipType;
import com.example.carnation.domain.care.dto.CaregiverRequestDto;
import com.example.carnation.domain.care.dto.PatientRequestDto;
import com.example.carnation.domain.user.dto.SigninRequestDto;
import com.example.carnation.domain.user.dto.SignupRequestDto;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDate;
import java.util.List;

public class TestInfo {
    private final static String ROOT_DUMP_URL = "src/test/java/com/example/carnation/domain/dump";

    public static SignupRequestDto getSignupRequestDto1() {
        return new SignupRequestDto(
                "test@naver.com1",
                "!@Skdud3401",
                "testNickname1",
                null,
                null
        );
    }

    public static SigninRequestDto getSigninRequestDto1() {
        return new SigninRequestDto(
                "test@naver.com1",
                "!@Skdud3401"
        );
    }

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

    // 실제 파일을 MultipartFile로 변환하는 메서드
    public static MultipartFile getMultipartFile(String path, String contentType) {
        try {
            File file = new File(path);
            return new MockMultipartFile(
                    file.getName(),       // 원본 파일명
                    file.getName(),       // 업로드 시 전달될 파일명
                    contentType,          // MIME 타입
                    new FileInputStream(file) // 파일 데이터
            );
        } catch (Exception e) {
            return null;
        }
    }

    // 이미지 및 동영상 파일 리스트 생성 (테스트용)
    public static List<MultipartFile> getTestImages() {
        try {
            return List.of(
                    getMultipartFile(ROOT_DUMP_URL + "/images/test_image1.png", "image/png"),
                    getMultipartFile(ROOT_DUMP_URL + "/images/test_image2.png", "image/png")
            );
        } catch (Exception e) {
            return null;
        }
    }

    public static List<MultipartFile> getTestVideos() {
        try {
            return List.of(
                    getMultipartFile(ROOT_DUMP_URL + "/videos/test_video1.mp4", "video/mp4"),
                    getMultipartFile(ROOT_DUMP_URL +  "/videos/test_video2.mp4", "video/mp4")
            );
        } catch (Exception e) {
            return null;
        }

    }
}
