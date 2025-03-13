package com.example.carnation.domain.user.service;

import com.example.carnation.annotation.SaveRefreshToken;
import com.example.carnation.common.exception.UserException;
import com.example.carnation.common.service.RedisService;
import com.example.carnation.domain.user.constans.AuthProvider;
import com.example.carnation.domain.user.cqrs.UserCommand;
import com.example.carnation.domain.user.cqrs.UserQuery;
import com.example.carnation.domain.user.dto.SigninRequestDto;
import com.example.carnation.domain.user.dto.SignupRequestDto;
import com.example.carnation.domain.user.entity.User;
import com.example.carnation.security.AuthUser;
import com.example.carnation.security.JwtDto;
import com.example.carnation.security.JwtManager;
import com.example.carnation.security.TokenDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.example.carnation.common.response.enums.UserApiResponse.EXISTING_SOCIAL_ACCOUNT;
import static com.example.carnation.common.response.enums.UserApiResponse.INVALID_CREDENTIALS;


@Service
@Slf4j(topic = "UserService")
@RequiredArgsConstructor
public class UserService {
    private final UserQuery userQuery;
    private final UserCommand userCommand;
    private final PasswordEncoder pe;
    private final JwtManager jm;
    private final RedisService redisService;

    public User signUp(final SignupRequestDto dto) {
        userQuery.validateEmailUniqueness(dto.getEmail());

        String encodedPassword = pe.encode(dto.getPassword());

        User user = User.of(dto,encodedPassword);

        return userCommand.save(user);
    }


    @SaveRefreshToken
    public TokenDto signin(final SigninRequestDto dto) {
        User user = userQuery.findByEmail(dto.getEmail());
        // 소셜 계정일 경우
        if (!user.getAuthProvider().equals(AuthProvider.GENERAL)) {
            throw new UserException(EXISTING_SOCIAL_ACCOUNT);
        }

        if (!pe.matches(dto.getPassword(), user.getPassword())) {
            throw new UserException(INVALID_CREDENTIALS);
        }

        String accessToken = jm.generateAccessToken(JwtDto.of(user));
        String refreshToken = jm.generateRefreshToken(JwtDto.of(user));
        return TokenDto.of(user.getId(), accessToken, refreshToken);
    }


    public void signout(AuthUser authUser) {
        User user = User.of(authUser);
        redisService.deleteRefreshToken(user.getId());
    }
}

