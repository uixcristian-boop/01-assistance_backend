package com.asistencia.backend.course.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

// Modelo de dominio para cursos academicos
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Course {
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
