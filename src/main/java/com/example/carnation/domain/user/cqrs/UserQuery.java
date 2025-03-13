package com.example.carnation.domain.user.cqrs;

import com.example.carnation.common.exception.UserException;
import com.example.carnation.common.response.enums.UserApiResponse;
import com.example.carnation.domain.user.entity.User;
import com.example.carnation.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.example.carnation.common.response.enums.UserApiResponse.USER_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class UserQuery {
    private final UserRepository repository;

    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public void validateEmailUniqueness(String email){
        if (repository.existsByEmail(email)) {
            throw new UserException(UserApiResponse.EMAIL_ALREADY_EXISTS);
        }
    }

    public Boolean existsByEmail(String email){
        return repository.existsByEmail(email);
    }
}
