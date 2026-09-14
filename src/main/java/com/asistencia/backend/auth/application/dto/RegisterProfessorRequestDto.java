package com.asistencia.backend.auth.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO para registro de profesor
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterProfessorRequestDto {
    @NotBlank(message = "El nombre completo es obligatorio")
    private String fullName;

    @NotBlank(message = "El correo electronico es obligatorio")
    @Email(message = "El formato de correo no es valido")
    private String email;
}
