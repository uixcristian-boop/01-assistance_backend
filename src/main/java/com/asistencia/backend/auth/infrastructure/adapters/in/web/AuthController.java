package com.asistencia.backend.auth.infrastructure.adapters.in.web;

import com.asistencia.backend.auth.application.dto.AuthResponseDto;
import com.asistencia.backend.auth.application.dto.LoginRequestDto;
import com.asistencia.backend.auth.application.dto.RegisterProfessorRequestDto;
import com.asistencia.backend.auth.domain.port.in.AuthUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Controlador REST para operaciones de autenticacion y registro
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthUseCase authUseCase;

    // Endpoint para inicio de sesion
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginRequestDto request) {
        AuthResponseDto response = authUseCase.login(request);
        return ResponseEntity.ok(response);
    }

    // Endpoint para registrar profesores con envio de credenciales al correo
    @PostMapping("/register-professor")
    public ResponseEntity<AuthResponseDto> registerProfessor(@Valid @RequestBody RegisterProfessorRequestDto request) {
        AuthResponseDto response = authUseCase.registerProfessor(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
