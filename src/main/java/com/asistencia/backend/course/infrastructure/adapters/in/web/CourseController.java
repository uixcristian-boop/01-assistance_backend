package com.asistencia.backend.course.infrastructure.adapters.in.web;

import com.asistencia.backend.course.application.dto.CourseResponseDto;
import com.asistencia.backend.course.application.dto.CreateCourseRequestDto;
import com.asistencia.backend.course.application.dto.UpdateCourseRequestDto;
import com.asistencia.backend.course.domain.port.in.CourseUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

// Controlador REST para la gestion de cursos del profesor
@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseUseCase courseUseCase;

    // Crea un nuevo curso asignado al profesor autenticado
    @PostMapping
    public ResponseEntity<CourseResponseDto> createCourse(
            @Valid @RequestBody CreateCourseRequestDto request,
            Principal principal
    ) {
        String professorEmail = principal.getName();
        CourseResponseDto created = courseUseCase.createCourse(request, professorEmail);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // Actualiza un curso existente del profesor autenticado
    @PutMapping("/{id}")
    public ResponseEntity<CourseResponseDto> updateCourse(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCourseRequestDto request,
            Principal principal
    ) {
        String professorEmail = principal.getName();
        CourseResponseDto updated = courseUseCase.updateCourse(id, request, professorEmail);
        return ResponseEntity.ok(updated);
    }

    // Lista todos los cursos del profesor autenticado
    @GetMapping
    public ResponseEntity<List<CourseResponseDto>> getMyCourses(Principal principal) {
        String professorEmail = principal.getName();
        List<CourseResponseDto> courses = courseUseCase.getCoursesByProfessor(professorEmail);
        return ResponseEntity.ok(courses);
    }
}
