package com.asistencia.backend.attendance.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

// Modelo de dominio para representar una sesion de clase con QR activo
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceSession {
    private Long id;
    private Long courseId;
    private LocalDate sessionDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer toleranceMinutes;
    private Integer lateThresholdMinutes;
    private String sessionToken;
    private boolean active;
    private LocalDateTime createdAt;
}
