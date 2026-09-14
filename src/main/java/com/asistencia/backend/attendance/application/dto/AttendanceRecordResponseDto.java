package com.asistencia.backend.attendance.application.dto;

import com.asistencia.backend.attendance.domain.model.AttendanceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// DTO para respuesta con datos del registro individual de asistencia
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceRecordResponseDto {
    private Long id;
    private Long sessionId;
    private Long studentId;
    private String studentCode;
    private String studentName;
    private AttendanceStatus status;
    private LocalDateTime checkInTime;
    private Double confidenceScore;
    private String capturePhotoUrl;
    private LocalDateTime createdAt;
}
