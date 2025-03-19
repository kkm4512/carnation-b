package com.example.carnation.domain.user.common.cqrs;

import com.example.carnation.common.exception.UserException;
import com.example.carnation.domain.user.common.entity.User;
import com.example.carnation.domain.user.common.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.example.carnation.common.response.enums.UserApiResponseEnum.USER_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class UserQuery {
    private final UserRepository repository;

    @Transactional(readOnly = true)
    public User readByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public User readById(Long id) {
        return repository.findById(id).orElseThrow(() -> new UserException(USER_NOT_FOUND));
    }

    // 이메일이 존재하면 true 반환
    @Transactional(readOnly = true)
    public Boolean existsByEmail(String email){
        return repository.existsByEmail(email);
    }
}
