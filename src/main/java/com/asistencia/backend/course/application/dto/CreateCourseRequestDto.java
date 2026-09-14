package com.asistencia.backend.course.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO para solicitud de creacion de curso
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCourseRequestDto {
    @NotBlank(message = "El nombre del curso es obligatorio")
    private String name;

    @NotBlank(message = "El codigo del curso es obligatorio")
    private String code;

    @NotBlank(message = "La seccion es obligatoria")
    private String section;

    private Integer credits;

    private String type;

    private String schedule;
}
