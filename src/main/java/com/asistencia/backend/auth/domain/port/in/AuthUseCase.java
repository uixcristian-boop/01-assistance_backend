package com.asistencia.backend.auth.domain.port.in;

import com.asistencia.backend.auth.application.dto.AuthResponseDto;
import com.asistencia.backend.auth.application.dto.LoginRequestDto;
import com.asistencia.backend.auth.application.dto.RegisterProfessorRequestDto;
import com.asistencia.backend.auth.application.dto.UpdateProfileRequestDto;

// Puerto de entrada para casos de uso de autenticacion y perfil
public interface AuthUseCase {
    AuthResponseDto login(LoginRequestDto request);
    AuthResponseDto registerProfessor(RegisterProfessorRequestDto request);
    AuthResponseDto getProfile(String email);
    AuthResponseDto updateProfile(UpdateProfileRequestDto request, String email);
}
