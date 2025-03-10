package com.example.carnation.domain.user.entity;

import com.example.carnation.common.exception.UserException;
import com.example.carnation.domain.careRecord.entity.CareRecord;
import com.example.carnation.domain.user.constans.UserType;
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

import static com.example.carnation.common.response.enums.UserApiResponse.NOT_ME;

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
    @Column(nullable = false, unique = true)
    private String nickname;

    /** 사용자 이메일 (고유값, 필수) */
    @Schema(description = "사용자 이메일 (고유값, 필수)", example = "nayoyn440@naerv.com")
    @Column(nullable = false, unique = true)
    private String email;

    /** 비밀번호 (해시 암호화 저장) */
    @Schema(description = "비밀번호 (해시 암호화 저장)", example = "$2a$10$TLs.cok52zPsTb/sfDv.PusUh8FJaTCAnqE1OnQNBywNjXGmJbxHG")
    @Column(nullable = false)
    private String password;

    /** 사용자 역할 (ADMIN, USER 등) */
    @Schema(description = "사용자 역할 (ROLE_ADMIN, ROLE_USER 등)", example = "ROLE_USER")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole userRole;

    /** 사용자 유형 (CAREGIVER, PATIENT 등) */
    @Schema(description = "사용자 유형 (CAREGIVER, PATIENT 등)", example = "CAREGIVER")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserType userType;

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

    /** 사용자가 작성한 간병 기록 목록 */
    @Schema(description = "사용자가 작성한 간병 기록 목록")
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CareRecord> careRecords = new ArrayList<>();

    public User(Long id, String nickname, String email, UserRole userRole, UserType userType) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.userRole = userRole != null ? userRole : UserRole.ROLE_USER; // 기본값 적용
        this.userType = userType != null ? userType : UserType.CAREGIVER; // 기본값 적용
    }

    public User(String nickname, String email, String password, UserRole userRole, UserType userType) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.userRole = userRole != null ? userRole : UserRole.ROLE_USER; // 기본값 적용
        this.userType = userType != null ? userType : UserType.CAREGIVER; // 기본값 적용
    }

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
                userRole,
                authUser.getUserType()
        );
    }

    public void isMe(Long id){
        if (this.id.longValue() != id) {
            throw new UserException(NOT_ME);
        }
    }
}
