package com.asistencia.backend.auth.domain.port.out;

import com.asistencia.backend.auth.domain.model.User;
import java.util.Optional;

// Puerto de salida para persistencia de usuarios
public interface UserRepositoryPort {
    User save(User user);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
