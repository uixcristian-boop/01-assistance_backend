package com.asistencia.backend.course.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// DTO para respuesta con datos del curso
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseResponseDto {
    private Long id;
    private String name;
    private String code;
    private String section;
    private Integer credits;
    private String type;
    private String schedule;
    private Long professorId;
    private boolean active;
    private LocalDateTime createdAt;
}
