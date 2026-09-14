package com.asistencia.backend.attendance.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

// DTO para la informacion de una sesion con resumen y lista en vivo de asistencia
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceSessionResponseDto {
    private Long id;
    private Long courseId;
    private String courseName;
    private String courseCode;
    private LocalDate sessionDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer toleranceMinutes;
    private Integer lateThresholdMinutes;
    private String sessionToken;
    private boolean active;
    private int totalStudents;
    private int presentCount;
    private int lateCount;
    private int absentCount;
    private List<AttendanceRecordResponseDto> records;
    private LocalDateTime createdAt;
}
