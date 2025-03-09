package com.example.carnation.domain.user.service;

import com.example.carnation.common.exception.UserException;
import com.example.carnation.common.response.enums.UserApiResponse;
import com.example.carnation.domain.user.dto.req.SendVerificationCodeDto;
import com.example.carnation.domain.user.dto.req.SigninRequestDto;
import com.example.carnation.domain.user.dto.req.SignupRequestDto;
import com.example.carnation.domain.user.entity.User;
import com.example.carnation.domain.user.repository.UserRepository;
import com.example.carnation.security.JwtDto;
import com.example.carnation.security.JwtManager;
import com.example.carnation.security.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

import static com.example.carnation.common.response.enums.UserApiResponse.INVALID_CREDENTIALS;
import static com.example.carnation.common.response.enums.UserApiResponse.USER_NOT_FOUND;


@Service
@Slf4j(topic = "UserService")
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder pe;
    private final JwtManager jm;

    @Transactional
    public void signUp(SignupRequestDto dto) {
        existsByEmail(dto.getEmail());

        String encodedPassword = pe.encode(dto.getPassword());

        User user = User.builder()
                .email(dto.getEmail())
                .password(encodedPassword)
                .nickname(dto.getNickname())
                .userRole(UserRole.ROLE_USER)
                .build();

        userRepository.save(user);
    }



    @Transactional
    public String signin(SigninRequestDto dto) {
        User user = findByEmail(dto.getEmail());

        if (!pe.matches(dto.getPassword(), user.getPassword())) {
            throw new UserException(INVALID_CREDENTIALS);
        }

        return jm.generateJwt(JwtDto.of(user));
    }


    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));
    }

    public void existsByEmail(String email){
        if (userRepository.existsByEmail(email)) {
            throw new UserException(UserApiResponse.EMAIL_ALREADY_EXISTS);
        }
    }
}

