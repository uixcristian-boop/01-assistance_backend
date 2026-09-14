package com.asistencia.backend.auth.infrastructure.adapters.out.persistence;

import com.asistencia.backend.auth.domain.model.User;
import org.springframework.stereotype.Component;

// Mapeador entre la entidad de dominio y la entidad JPA
@Component
public class UserMapper {

    // Convierte entidad de dominio a entidad JPA
    public UserJpaEntity toJpaEntity(User domain) {
        if (domain == null) {
            return null;
        }
        return UserJpaEntity.builder()
                .id(domain.getId())
                .fullName(domain.getFullName())
                .email(domain.getEmail())
                .password(domain.getPassword())
                .role(domain.getRole())
                .profilePicture(domain.getProfilePicture())
                .active(domain.isActive())
                .createdAt(domain.getCreatedAt())
                .build();
    }

    // Convierte entidad JPA a modelo de dominio
    public User toDomain(UserJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        return User.builder()
                .id(entity.getId())
                .fullName(entity.getFullName())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .role(entity.getRole())
                .profilePicture(entity.getProfilePicture())
                .active(entity.isActive())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
