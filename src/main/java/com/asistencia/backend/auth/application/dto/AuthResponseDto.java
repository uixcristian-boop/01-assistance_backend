package com.asistencia.backend.auth.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO para respuesta de autenticacion
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDto {
    private String token;
    private String type;
    private Long id;
    private String fullName;
    private String email;
    private String role;
    private String profilePicture;
    private String message;
}
