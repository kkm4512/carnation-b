package com.example.carnation.domain.user.service;

import com.example.carnation.common.exception.UserException;
import com.example.carnation.domain.user.cqrs.UserCommand;
import com.example.carnation.domain.user.cqrs.UserQuery;
import com.example.carnation.domain.user.dto.SigninRequestDto;
import com.example.carnation.domain.user.dto.SignupRequestDto;
import com.example.carnation.domain.user.entity.User;
import com.example.carnation.security.JwtDto;
import com.example.carnation.security.JwtManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.example.carnation.common.response.enums.UserApiResponse.INVALID_CREDENTIALS;


@Service
@Slf4j(topic = "UserService")
@RequiredArgsConstructor
public class UserService {
    private final UserQuery userQuery;
    private final UserCommand userCommand;
    private final PasswordEncoder pe;
    private final JwtManager jm;

    public User signUp(final SignupRequestDto dto) {
        userQuery.existsByEmail(dto.getEmail());

        String encodedPassword = pe.encode(dto.getPassword());

        User user = new User(
                dto.getNickname(),
                dto.getEmail(),
                encodedPassword,
                dto.getUserRole(),
                dto.getUserType()
        );

        return userCommand.save(user);
    }


    public String signin(final SigninRequestDto dto) {
        User user = userQuery.findByEmail(dto.getEmail());

        if (!pe.matches(dto.getPassword(), user.getPassword())) {
            throw new UserException(INVALID_CREDENTIALS);
        }

        return jm.generateJwt(JwtDto.of(user));
    }



}

