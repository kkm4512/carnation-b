package com.example.carnation.domain.user.auth.service;

import com.example.carnation.annotation.RotateToken;
import com.example.carnation.annotation.SaveRefreshToken;
import com.example.carnation.annotation.SaveVerificationCode;
import com.example.carnation.common.exception.UserException;
import com.example.carnation.common.service.RedisService;
import com.example.carnation.domain.user.auth.dto.*;
import com.example.carnation.domain.user.auth.helper.SmsHelper;
import com.example.carnation.domain.user.auth.helper.TokenHelper;
import com.example.carnation.domain.user.auth.helper.VerificationHelper;
import com.example.carnation.domain.user.common.cqrs.UserCommand;
import com.example.carnation.domain.user.common.cqrs.UserQuery;
import com.example.carnation.domain.user.common.entity.User;
import com.example.carnation.security.AuthUser;
import com.example.carnation.security.JwtDto;
import com.example.carnation.security.JwtManager;
import com.example.carnation.security.TokenDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.carnation.common.response.enums.UserApiResponseEnum.EMAIL_ALREADY_EXISTS;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "AuthService")
public class AuthService {
    private final SmsHelper smsHelper;
    private final RedisService redisService;
    private final UserQuery userQuery;
    private final UserCommand userCommand;
    private final PasswordEncoder pe;
    private final JwtManager jwtManager;
    private final TokenHelper tokenHelper;
    private final VerificationHelper verificationHelper;

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
    public TokenDto signin(final SigninRequestDto dto) {
        User user = userQuery.readByEmail(dto.getEmail());
        user.validateNotSocialAccount();
        verificationHelper.verifyPassword(dto.getPassword(), user.getPassword());
        return tokenHelper.createTokenDto(user);
    }


    public void signout(final AuthUser authUser) {
        User user = User.of(authUser);
        redisService.deleteRefreshToken(user.getId());
    }

    @SaveVerificationCode
    public PhoneAuthResponseDto sendVerificationCode(final PhoneAuthRequestDto dto) {
        String verificationCode = verificationHelper.generateVerificationCode(); // 4자리 랜덤 숫자 생성
        smsHelper.sendSms(dto.getPhoneNumber(), verificationCode);
        return PhoneAuthResponseDto.of(dto.getPhoneNumber(), verificationCode);
    }

    public void verifyCode(final PhoneAuthVerifyRequestDto dto) {
        String expectedVerificationCode = dto.getVerificationCode();
        String actualVerificationCode = redisService.getVerificationCode(dto.getPhoneNumber());
        verificationHelper.validateVerificationCode(expectedVerificationCode, actualVerificationCode, dto.getPhoneNumber());
    }

    @RotateToken
    public TokenDto refreshToken(final TokenRefreshRequestDto dto) {
        jwtManager.validateToken(dto.getRefreshToken());
        Long userId = Long.valueOf(jwtManager.toClaims(dto.getRefreshToken()).getSubject());
        String refreshToken = redisService.getRefreshToken(userId);
        jwtManager.compare(dto.getRefreshToken(),refreshToken);
        User user = userQuery.readById(userId);
        String newAccessToken = jwtManager.generateAccessToken(JwtDto.of(user));
        String newRefreshToken = jwtManager.generateRefreshToken(JwtDto.of(user));
        return TokenDto.of(
                user.getId(),
                newAccessToken,
                newRefreshToken
        );
    }


}
