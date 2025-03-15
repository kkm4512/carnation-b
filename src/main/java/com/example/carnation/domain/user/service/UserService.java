package com.example.carnation.domain.user.service;

import com.example.carnation.annotation.SaveRefreshToken;
import com.example.carnation.common.exception.UserException;
import com.example.carnation.common.service.RedisService;
import com.example.carnation.domain.token.service.TokenService;
import com.example.carnation.domain.user.cqrs.UserCommand;
import com.example.carnation.domain.user.cqrs.UserQuery;
import com.example.carnation.domain.user.dto.SigninRequestDto;
import com.example.carnation.domain.user.dto.SignupRequestDto;
import com.example.carnation.domain.user.dto.UserResponseDto;
import com.example.carnation.domain.user.entity.User;
import com.example.carnation.security.AuthUser;
import com.example.carnation.security.JwtManager;
import com.example.carnation.security.TokenDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.carnation.common.response.enums.UserApiResponseEnum.EMAIL_ALREADY_EXISTS;
import static com.example.carnation.common.response.enums.UserApiResponseEnum.INVALID_CREDENTIALS;


@Service
@Slf4j(topic = "UserService")
@RequiredArgsConstructor
public class UserService {
    private final UserQuery userQuery;
    private final UserCommand userCommand;
    private final PasswordEncoder pe;
    private final JwtManager jm;
    private final RedisService redisService;
    private final TokenService tokenService;

    @Transactional
    public UserResponseDto signUp(final SignupRequestDto dto) {
        Boolean flag = userQuery.existsByEmail(dto.getEmail());
        // 중복된 이메일이 없을 경우
        if (!flag) {
            String encodedPassword = pe.encode(dto.getPassword());
            User user = User.of(dto,encodedPassword);
            User saveUser = userCommand.create(user);
            return UserResponseDto.of(saveUser);
        }
        else {
            throw new UserException(EMAIL_ALREADY_EXISTS);
        }

    }


    @SaveRefreshToken
    @Transactional(readOnly = true)
    public TokenDto signin(final SigninRequestDto dto) {
        User user = userQuery.readByEmail(dto.getEmail());
        user.validateNotSocialAccount();
        verifyPassword(dto.getPassword(), user.getPassword());
        return tokenService.createTokenDto(user);
    }


    @Transactional
    public void signout(final AuthUser authUser) {
        User user = User.of(authUser);
        redisService.deleteRefreshToken(user.getId());
    }

    public void verifyPassword(final String rawPassword, final String encodedPassword) {
        if (!pe.matches(rawPassword,encodedPassword)) {
            throw new UserException(INVALID_CREDENTIALS);
        }
    }
}

