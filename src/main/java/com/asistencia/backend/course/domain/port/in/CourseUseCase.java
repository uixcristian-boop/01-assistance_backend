package com.asistencia.backend.course.domain.port.in;

import com.asistencia.backend.course.application.dto.CourseResponseDto;
import com.asistencia.backend.course.application.dto.CreateCourseRequestDto;
import com.asistencia.backend.course.application.dto.UpdateCourseRequestDto;

import java.util.List;

// Puerto de entrada para casos de uso de gestion de cursos
public interface CourseUseCase {
    CourseResponseDto createCourse(CreateCourseRequestDto request, String professorEmail);
    CourseResponseDto updateCourse(Long courseId, UpdateCourseRequestDto request, String professorEmail);
    List<CourseResponseDto> getCoursesByProfessor(String professorEmail);
}
