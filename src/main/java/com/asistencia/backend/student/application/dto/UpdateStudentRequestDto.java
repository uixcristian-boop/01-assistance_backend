package com.asistencia.backend.student.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO para actualizar los datos de un alumno
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStudentRequestDto {

    @NotBlank(message = "El codigo del alumno es obligatorio")
    @Size(max = 30, message = "El codigo no debe exceder los 30 caracteres")
    private String code;

    @NotBlank(message = "El nombre del alumno es obligatorio")
    @Size(max = 100, message = "El nombre no debe exceder los 100 caracteres")
    private String firstName;

    @NotBlank(message = "Los apellidos del alumno son obligatorios")
    @Size(max = 100, message = "Los apellidos no deben exceder los 100 caracteres")
    private String lastName;

    @Email(message = "El formato de correo no es valido")
    @Size(max = 150, message = "El correo no debe exceder los 150 caracteres")
    private String email;
}
