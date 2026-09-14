package com.asistencia.backend.attendance.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

// Modelo de dominio para representar el marcaje de asistencia de un alumno
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceRecord {
    private Long id;
    private Long sessionId;
    private Long studentId;
    private AttendanceStatus status;
    private LocalDateTime checkInTime;
    private Double confidenceScore;
    private String capturePhotoUrl;
    private LocalDateTime createdAt;
}
