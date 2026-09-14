package com.asistencia.backend.auth.infrastructure.adapters.in.web;

import com.asistencia.backend.auth.application.dto.AuthResponseDto;
import com.asistencia.backend.auth.application.dto.UpdateProfileRequestDto;
import com.asistencia.backend.auth.domain.port.in.AuthUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Controlador REST para gestion del perfil de usuario autenticado
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final AuthUseCase authUseCase;

    // Obtiene el perfil del usuario autenticado
    @GetMapping("/profile")
    public ResponseEntity<AuthResponseDto> getProfile(Authentication authentication) {
        AuthResponseDto profile = authUseCase.getProfile(authentication.getName());
        return ResponseEntity.ok(profile);
    }

    // Actualiza el perfil (nombre y foto) del usuario autenticado
    @PutMapping("/profile")
    public ResponseEntity<AuthResponseDto> updateProfile(
            @Valid @RequestBody UpdateProfileRequestDto request,
            Authentication authentication) {
        AuthResponseDto updated = authUseCase.updateProfile(request, authentication.getName());
        return ResponseEntity.ok(updated);
    }
}
