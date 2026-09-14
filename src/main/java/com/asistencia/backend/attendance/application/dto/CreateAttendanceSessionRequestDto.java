package com.asistencia.backend.attendance.application.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

// DTO para iniciar una nueva sesion de clase con QR de asistencia
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAttendanceSessionRequestDto {

    @NotNull(message = "La hora de inicio es obligatoria")
    private LocalTime startTime;

    @NotNull(message = "La hora de fin es obligatoria")
    private LocalTime endTime;

    @Builder.Default
    private Integer toleranceMinutes = 15;

    @Builder.Default
    private Integer lateThresholdMinutes = 30;
}
