package com.asistencia.backend.student.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// DTO para respuesta con datos del alumno
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentResponseDto {
    private Long id;
    private Long courseId;
    private String code;
    private String firstName;
    private String lastName;
    private String email;
    private boolean hasFaceDescriptor;
    private String faceDescriptor;
    private String photoUrl;
    private boolean active;
    private LocalDateTime createdAt;
}
