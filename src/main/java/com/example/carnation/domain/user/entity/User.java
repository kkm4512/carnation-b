package com.example.carnation.domain.user.entity;

import com.example.carnation.common.exception.UserException;
import com.example.carnation.security.AuthUser;
import com.example.carnation.security.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;

import static com.example.carnation.common.response.enums.UserApiResponse.NOT_ME;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Slf4j(topic = "User")
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole userRole;

    @Column
    private String code;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column
    private LocalDateTime updatedAt;

    public User(Long id, String nickname, String email, UserRole userRole) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.userRole = userRole;
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
                userRole
        );
    }

    public void isMe(Long id){
        if (this.id.longValue() != id) {
            throw new UserException(NOT_ME);
        }
    }
}
