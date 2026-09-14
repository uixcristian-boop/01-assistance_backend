package com.asistencia.backend.student.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

// Modelo de dominio para representar a un alumno de un curso
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    private Long id;
    private Long courseId;
    private String code;
    private String firstName;
    private String lastName;
    private String email;
    private String faceDescriptor;
    private String photoUrl;
    private boolean active;
    private LocalDateTime createdAt;
}
