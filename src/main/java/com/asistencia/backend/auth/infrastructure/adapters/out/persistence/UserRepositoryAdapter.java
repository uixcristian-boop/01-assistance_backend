package com.asistencia.backend.auth.infrastructure.adapters.out.persistence;

import com.asistencia.backend.auth.domain.model.User;
import com.asistencia.backend.auth.domain.port.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

// Adaptador de persistencia que implementa el puerto UserRepositoryPort
@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final SpringDataUserRepository springDataUserRepository;
    private final UserMapper userMapper;

    @Override
    public User save(User user) {
        UserJpaEntity jpaEntity = userMapper.toJpaEntity(user);
        UserJpaEntity savedEntity = springDataUserRepository.save(jpaEntity);
        return userMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return springDataUserRepository.findByEmail(email)
                .map(userMapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return springDataUserRepository.existsByEmail(email);
    }
}
