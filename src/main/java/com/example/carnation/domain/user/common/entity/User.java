package com.example.carnation.domain.user.common.entity;

import com.example.carnation.common.exception.UserException;
import com.example.carnation.common.util.Validator;
import com.example.carnation.domain.care.entity.Caregiver;
import com.example.carnation.domain.care.entity.Patient;
import com.example.carnation.domain.product.entity.Product;
import com.example.carnation.domain.user.auth.dto.SignupRequestDto;
import com.example.carnation.domain.user.common.constans.AuthProvider;
import com.example.carnation.domain.user.oAuth.dto.OAuthUserDto;
import com.example.carnation.domain.user.wallet.constans.BankType;
import com.example.carnation.domain.user.wallet.entity.Wallet;
import com.example.carnation.security.AuthUser;
import com.example.carnation.security.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.carnation.common.response.enums.UserApiResponseEnum.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Slf4j(topic = "User")
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "사용자 고유 ID", example = "1")
    private Long id;

    /** 사용자 닉네임 (고유값, 필수) */
    @Schema(description = "사용자 닉네임 (고유값, 필수)", example = "카네이션123")
    @Column(nullable = false)
    private String nickname;

    /** 사용자 이메일 (고유값, 필수) */
    @Schema(description = "사용자 이메일 (고유값, 필수)", example = "nayoyn440@naerv.com")
    @Column(unique = true)
    private String email;

    /** 📌 사용자의 휴대폰 번호 */
    @Schema(
            description = "사용자의 휴대폰 번호 (고유값, 필수 입력)",
            example = "01012345678"
    )
    @Column(unique = true)
    private String phoneNumber;

    /** 비밀번호 (해시 암호화 저장) */
    @Schema(description = "비밀번호 (해시 암호화 저장)", example = "$2a$10$TLs.cok52zPsTb/sfDv.PusUh8FJaTCAnqE1OnQNBywNjXGmJbxHG")
    private String password;

    /** 사용자 역할 (ADMIN, USER 등) */
    @Schema(description = "사용자 역할 (ROLE_ADMIN, ROLE_USER 등)", example = "ROLE_USER")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole userRole;

    @Column(unique = true, length = 14)
    @Schema(description = "주민등록번호 (14자리)", example = "850101-2345678")
    private String residentRegistrationNumber;

    @Schema(
            description = "사용자 로그인 제공자 (KAKAO, NAVER, GOOGLE, GENERAL 등)",
            example = "KAKAO"
    )
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuthProvider authProvider;

    /** 생성 날짜 (자동 입력) */
    @Schema(description = "생성 날짜 (자동 입력)", example = "2024-03-01T10:00:00")
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    /** 마지막 수정 날짜 (자동 입력) */
    @Schema(description = "마지막 수정 날짜 (자동 입력)", example = "2024-03-01T10:00:00")
    @LastModifiedDate
    @Column
    private LocalDateTime updatedAt;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "patient_id", unique = true)
    @Schema(description = "유저 - 환자 매핑 (UserPatient)", example = "1")
    private Patient patient;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "caregiver_id", unique = true)
    @Schema(description = "유저 - 간병인 매핑 (UserPatient)", example = "1")
    private Caregiver caregiver;

    /** 💰 사용자 계좌 정보 */
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_wallet_id", unique = true)
    private Wallet userWallet;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products = new ArrayList<>();

    // AuthUser -> User
    public User(Long id, String nickname, String email, UserRole userRole) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.userRole = userRole != null ? userRole : UserRole.ROLE_USER; // 기본값 적용
    }

    // User Test
    public User(Long id, String nickname, String email, String phoneNumber, String password, UserRole userRole, String residentRegistrationNumber, AuthProvider authProvider, LocalDateTime createdAt, LocalDateTime updatedAt, Patient patient, Caregiver caregiver, BankType bank, String accountNumber) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.userRole = userRole;
        this.residentRegistrationNumber = residentRegistrationNumber;
        this.authProvider = authProvider;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.patient = patient;
        this.caregiver = caregiver;
        this.userWallet = Wallet.of(this,bank,accountNumber);
    }

    // 일반 회원가입
    public User(String nickname, String email, String password, String phoneNumber, UserRole userRole, String residentRegistrationNumber, BankType bank, String accountNumber) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.userRole = userRole != null ? userRole : UserRole.ROLE_USER; // 기본값 적용
        this.authProvider = AuthProvider.GENERAL;
        this.residentRegistrationNumber = residentRegistrationNumber;
        this.userWallet = Wallet.of(this,bank,accountNumber);
    }

    // 소셜 회원가입
    public User(String email, String nickname, AuthProvider authProvider) {
        this.email = email;
        this.nickname = nickname;
        this.userRole = UserRole.ROLE_USER; // 기본값 적용
        this.authProvider = authProvider;
        this.userWallet = Wallet.of(this);
    }

    // AuthUser -> User
    public static User of(AuthUser authUser){
        String roleName = authUser.getAuthorities()
                .stream()
                .findFirst() // 첫 번째 권한만 사용
                .map(GrantedAuthority::getAuthority)
                .orElseThrow(() -> new IllegalArgumentException("No authority found"));

        UserRole userRole = UserRole.valueOf(roleName); // 문자열을 UserRole Enum으로 변환
        return new User(
                authUser.getUserId(),
                authUser.getNickname(),
                authUser.getEmail(),
                userRole
        );
    }

    // 소셜 회원가입 - of
    public static User of(OAuthUserDto dto, AuthProvider authProvider){
        return new User(
                dto.getEmail(),
                dto.getNickname(),
                authProvider
        );
    }

    // 일반 회원가입 - of
    public static User of(SignupRequestDto dto, String encodedPassword){
        return new User(
            dto.getNickname(),
            dto.getEmail(),
            encodedPassword,
            dto.getPhoneNumber(),
            dto.getUserRole(),
            dto.getResidentRegistrationNumber(),
            dto.getBank(),
            dto.getAccountNumber()
        );
    }

    public void isNotMe(User user) {
        Validator.validateNotNullAndNotEqual(
            this.id,
            user.getId(),
            new UserException(NOT_ME)
        );
    }

    public void isMe(User user) {
        Validator.validateNotNullAndEqual(
                this.id,
                user.getId(),
                new UserException(INVALID_SELF_OPERATION)
        );
    }

    // 현재 계정이 소셜 계정이 아닌지 검증
    public void validateNotSocialAccount() {
        if (!this.authProvider.equals(AuthProvider.GENERAL)) {
            throw new UserException(EXISTING_SOCIAL_ACCOUNT);
        }
    }


    public void updateCareGiver(Caregiver caregiver) {
        this.caregiver = caregiver;
    }

    public void updatePatient(Patient patient) {
        this.patient = patient;
    }

}
