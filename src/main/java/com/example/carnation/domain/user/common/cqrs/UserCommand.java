package com.example.carnation.domain.user.common.cqrs;

import com.example.carnation.domain.user.common.entity.User;
import com.example.carnation.domain.user.common.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UserCommand {
    private final UserRepository repository;

    @Transactional
    public User create(User entity) {
        return repository.save(entity);
    }
}
