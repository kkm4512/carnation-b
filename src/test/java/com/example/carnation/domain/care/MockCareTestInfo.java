package com.example.carnation.domain.care;

import com.example.carnation.domain.care.dto.CareMatchingRequestDto;
import com.example.carnation.domain.care.dto.CaregiverRequestDto;
import com.example.carnation.domain.care.dto.PatientRequestDto;
import com.example.carnation.domain.care.entity.CareMatching;
import com.example.carnation.domain.care.entity.Caregiver;
import com.example.carnation.domain.care.entity.Patient;
import com.example.carnation.domain.user.common.entity.User;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.util.List;

import static com.example.carnation.domain.user.MockUserInfo.getUser1;
import static com.example.carnation.domain.user.MockUserInfo.getUser2;

public class MockCareTestInfo {
    private static final String ROOT_DUMP_URL = "src/test/java/com/example/carnation/domain/dump";

    // 공통된 테스트 데이터 상수 선언
    private static final Long ID_1 = 1L;
    private static final Long ID_2 = 2L;
    private static final LocalDateTime NOW = LocalDateTime.now();


    public static CaregiverRequestDto getCaregiverRequestDto1() {
        return new CaregiverRequestDto("이영희", 170.5, 65.3, true);
    }

    public static CaregiverRequestDto getCaregiverRequestDto2() {
        return new CaregiverRequestDto("이영희2", 175.0, 70.2, true);
    }

    public static PatientRequestDto getPatientRequestDto1() {
        return new PatientRequestDto("김철수", "서울 x 병원", "감기", true);
    }

    public static PatientRequestDto getPatientRequestDto2() {
        return new PatientRequestDto("김철수2", "인천 x 병원", "감기", true);
    }

    // 2. Patient와 Caregiver를 생성하고, User와 연결
    public static Patient getPatient1() {
        User user = getUser1(); // 미리 생성된 User를 가져옴
        return new Patient(ID_1, "김철수", "서울 x 병원", "감기", true, true, NOW, NOW, user, null);
    }

    public static Patient getPatient2() {
        User user = getUser2();
        return new Patient(ID_2, "김철수2", "인천 x 병원", "감기", true, true, NOW, NOW, user,null);
    }

    public static Caregiver getCaregiver1() {
        User user = getUser1();
        return new Caregiver(ID_1, "이영희", 170.5, 65.3, true, true, NOW, NOW, user, null);
    }

    public static Caregiver getCaregiver2() {
        User user = getUser2();
        return new Caregiver(ID_2, "이영희2", 175.0, 70.2, true, true, NOW, NOW, user, null);
    }

    // ✅ CareMatchingRequestDto (매칭 요청 DTO)
    public static CareMatchingRequestDto getCareMatchingRequestDtoForCaregiver1() {
        return new CareMatchingRequestDto(ID_1,NOW,NOW,10000);
    }

    public static CareMatchingRequestDto getCareMatchingRequestDtoForCaregiver2() {
        return new CareMatchingRequestDto(ID_2,NOW,NOW,10000);
    }

    public static CareMatchingRequestDto getCareMatchingRequestDtoForPatient1() {
        return new CareMatchingRequestDto(ID_1,NOW,NOW,10000);
    }

    public static CareMatchingRequestDto getCareMatchingRequestDtoForPatient2() {
        return new CareMatchingRequestDto(ID_2,NOW,NOW,10000);
    }

    // ✅ CareMatching (매칭 엔티티)
    public static CareMatching getCareMatching1() {
        return new CareMatching(getPatient1(), getCaregiver1(), 10000, NOW, NOW);
    }

    public static CareMatching getCareMatching2() {
        return new CareMatching(getPatient2(), getCaregiver2(), 10000, NOW, NOW);
    }

    // 실제 파일을 MultipartFile로 변환하는 메서드
    private static MultipartFile getMultipartFile(String path, String contentType) {
        try {
            File file = new File(path);
            return new MockMultipartFile(
                    file.getName(), file.getName(), contentType, new FileInputStream(file)
            );
        } catch (Exception e) {
            return null;
        }
    }

    public static List<MultipartFile> getTestImages() {
        return List.of(
                getMultipartFile(ROOT_DUMP_URL + "/images/test_image1.png", "image/png"),
                getMultipartFile(ROOT_DUMP_URL + "/images/test_image2.png", "image/png")
        );
    }

    public static List<MultipartFile> getTestVideos() {
        return List.of(
                getMultipartFile(ROOT_DUMP_URL + "/videos/test_video1.mp4", "video/mp4")
        );
    }
}
