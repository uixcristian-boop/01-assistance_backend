package com.asistencia.backend.attendance.application.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO para registrar el marcaje de asistencia de un alumno identificado
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckInRequestDto {

    @NotNull(message = "El id del alumno es obligatorio")
    private Long studentId;

    private Double confidenceScore;

    private String capturePhotoUrl;
}
