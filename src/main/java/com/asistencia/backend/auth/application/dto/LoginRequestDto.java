package com.asistencia.backend.auth.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO para solicitud de inicio de sesion
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDto {
    @NotBlank(message = "El correo electronico es obligatorio")
    @Email(message = "El formato de correo no es valido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;
}
