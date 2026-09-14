package com.asistencia.backend.auth.domain.port.out;

// Puerto de salida para codificacion y verificacion de contraseñas
public interface PasswordEncoderPort {
    String encode(CharSequence rawPassword);
    boolean matches(CharSequence rawPassword, String encodedPassword);
}
