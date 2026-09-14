package com.asistencia.backend.attendance.application.dto;

import com.asistencia.backend.student.application.dto.StudentResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

// DTO con datos publicos de la sesion para el escaneo del alumno
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublicSessionResponseDto {
    private String sessionToken;
    private String courseName;
    private String courseCode;
    private String section;
    private LocalDate sessionDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer toleranceMinutes;
    private Integer lateThresholdMinutes;
    private boolean active;
    private List<StudentResponseDto> students;
}
