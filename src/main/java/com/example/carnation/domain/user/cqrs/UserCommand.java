package com.example.carnation.domain.user.cqrs;

import com.example.carnation.domain.user.entity.User;
import com.example.carnation.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UserCommand {
    private final UserRepository repository;

    @Transactional
    public User save(User entity) {
        return repository.save(entity);
    }
}
