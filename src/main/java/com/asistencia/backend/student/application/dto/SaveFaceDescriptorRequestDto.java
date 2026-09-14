package com.asistencia.backend.student.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO para registrar el vector biometrico facial y foto de referencia del alumno
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveFaceDescriptorRequestDto {

    @NotBlank(message = "El descriptor facial es obligatorio")
    private String faceDescriptor;

    private String photoUrl;
}
