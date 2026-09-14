package com.asistencia.backend.auth.domain.port.out;

import com.asistencia.backend.auth.domain.model.User;

// Puerto de salida para generacion y validacion de tokens JWT
public interface TokenProviderPort {
    String generateToken(User user);
    String extractEmail(String token);
    boolean validateToken(String token);
}
