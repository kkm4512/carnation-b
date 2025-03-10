package com.example.carnation.security;

import com.example.carnation.domain.user.constans.UserType;
import com.example.carnation.domain.user.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.Serializable;
import java.security.Principal;
import java.util.Collection;
import java.util.List;

@Getter
public class AuthUser implements Principal, Serializable {
    private final Long userId;
    private final String email;
    private final String nickname;
    private final Collection<? extends GrantedAuthority> authorities;
    private final UserType userType;

    public AuthUser(Long userId, String email, String nickname, UserRole userRole, UserType userType) {
        this.userId = userId;
        this.email = email;
        this.nickname = nickname;
        this.authorities = List.of(new SimpleGrantedAuthority(userRole.name()));
        this.userType = userType;
    }

    @Override
    public String getName() {
        return String.valueOf(userId);
    }

    public static AuthUser of(User user) {
        return new AuthUser(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                user.getUserRole(),
                user.getUserType()
        );
    }
}
