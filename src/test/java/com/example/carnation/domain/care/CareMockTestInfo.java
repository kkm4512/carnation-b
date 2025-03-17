package com.example.carnation.domain.care;

import com.example.carnation.domain.care.constans.UserType;
import com.example.carnation.domain.care.dto.CareMatchingRequestDto;
import com.example.carnation.domain.care.dto.CaregiverRequestDto;
import com.example.carnation.domain.care.dto.PatientRequestDto;
import com.example.carnation.domain.care.entity.CareMatching;
import com.example.carnation.domain.care.entity.Caregiver;
import com.example.carnation.domain.care.entity.Patient;
import com.example.carnation.domain.user.constans.AuthProvider;
import com.example.carnation.domain.user.entity.User;
import com.example.carnation.security.AuthUser;
import com.example.carnation.security.UserRole;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.util.List;

public class CareMockTestInfo {
    private static final String ROOT_DUMP_URL = "src/test/java/com/example/carnation/domain/dump";

    // 공통된 테스트 데이터 상수 선언
    private static final Long ID_1 = 1L;
    private static final Long ID_2 = 2L;

    private static final String EMAIL_1 = "test@naver.com1";
    private static final String EMAIL_2 = "test@naver.com2";
    private static final String NICKNAME_1 = "testNickname1";
    private static final String NICKNAME_2 = "testNickname2";
    private static final String PASSWORD_1 = "!@Skdud3401";
    private static final String PASSWORD_2 = "!@Skdud3402";
    private static final String PHONE_1 = "01011111111";
    private static final String PHONE_2 = "01022222222";
    private static final String SSN_1 = "111111-1111111";
    private static final String SSN_2 = "222222-2222222";
    private static final UserRole ROLE = UserRole.ROLE_USER;
    private static final AuthProvider PROVIDER = AuthProvider.GENERAL;
    private static final LocalDateTime NOW = LocalDateTime.now();

    public static AuthUser getAuthUser1() {
        return new AuthUser(ID_1, EMAIL_1, NICKNAME_1, ROLE, PROVIDER);
    }

    public static AuthUser getAuthUser2() {
        return new AuthUser(ID_2, EMAIL_2, NICKNAME_2, ROLE, PROVIDER);
    }

    // 1. User를 먼저 생성 (Patient와 Caregiver는 나중에 설정)
    public static User getUser1() {
        return new User(ID_1, NICKNAME_1, EMAIL_1, PHONE_1, PASSWORD_1, ROLE, SSN_1, PROVIDER, NOW, NOW, null, null);
    }

    public static User getUser2() {
        return new User(ID_2, NICKNAME_2, EMAIL_2, PHONE_2, PASSWORD_2, ROLE, SSN_2, PROVIDER, NOW, NOW, null, null);
    }


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
        return new Patient(ID_1, "김철수", "서울 x 병원", "감기", true, NOW, NOW, user);
    }

    public static Patient getPatient2() {
        User user = getUser2();
        return new Patient(ID_2, "김철수2", "인천 x 병원", "감기", true, NOW, NOW, user);
    }

    public static Caregiver getCaregiver1() {
        User user = getUser1();
        return new Caregiver(ID_1, "이영희", 170.5, 65.3, true, NOW, NOW, user);
    }

    public static Caregiver getCaregiver2() {
        User user = getUser2();
        return new Caregiver(ID_2, "이영희2", 175.0, 70.2, true, NOW, NOW, user);
    }

    // ✅ CareMatchingRequestDto (매칭 요청 DTO)
    public static CareMatchingRequestDto getCareMatchingRequestDtoForCaregiver1() {
        return new CareMatchingRequestDto(ID_1,UserType.CAREGIVER,NOW,NOW,10000);
    }

    public static CareMatchingRequestDto getCareMatchingRequestDtoForCaregiver2() {
        return new CareMatchingRequestDto(ID_2,UserType.CAREGIVER,NOW,NOW,10000);
    }

    public static CareMatchingRequestDto getCareMatchingRequestDtoForPatient1() {
        return new CareMatchingRequestDto(ID_1,UserType.PATIENT,NOW,NOW,10000);
    }

    public static CareMatchingRequestDto getCareMatchingRequestDtoForPatient2() {
        return new CareMatchingRequestDto(ID_2,UserType.PATIENT,NOW,NOW,10000);
    }

    // ✅ CareMatching (매칭 엔티티)
    public static CareMatching getCareMatching1() {
        return new CareMatching(getPatient1(), getCaregiver1(), NOW, NOW);
    }

    public static CareMatching getCareMatching2() {
        return new CareMatching(getPatient2(), getCaregiver2(), NOW, NOW);
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
