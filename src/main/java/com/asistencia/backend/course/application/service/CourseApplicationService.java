package com.asistencia.backend.course.application.service;

import com.asistencia.backend.auth.domain.model.User;
import com.asistencia.backend.auth.domain.port.out.UserRepositoryPort;
import com.asistencia.backend.course.application.dto.CourseResponseDto;
import com.asistencia.backend.course.application.dto.CreateCourseRequestDto;
import com.asistencia.backend.course.application.dto.UpdateCourseRequestDto;
import com.asistencia.backend.course.domain.model.Course;
import com.asistencia.backend.course.domain.port.in.CourseUseCase;
import com.asistencia.backend.course.domain.port.out.CourseRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

// Servicio de aplicacion para la logica de negocio de cursos
@Service
@RequiredArgsConstructor
public class CourseApplicationService implements CourseUseCase {

    private final CourseRepositoryPort courseRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;

    // Crea un nuevo curso asignado al profesor autenticado
    @Override
    @Transactional
    public CourseResponseDto createCourse(CreateCourseRequestDto request, String professorEmail) {
        String email = professorEmail != null ? professorEmail.trim().toLowerCase() : "";
        User professor = userRepositoryPort.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Profesor no encontrado con correo: " + professorEmail));

        String normalizedCode = request.getCode().trim().toUpperCase();

        if (courseRepositoryPort.existsByCodeAndProfessorId(normalizedCode, professor.getId())) {
            throw new IllegalArgumentException("Ya existe un curso registrado con el codigo: " + normalizedCode);
        }

        Course course = Course.builder()
                .name(request.getName().trim())
                .code(normalizedCode)
                .section(request.getSection().trim().toUpperCase())
                .credits(request.getCredits())
                .type(request.getType() != null ? request.getType().trim().toUpperCase() : null)
                .schedule(request.getSchedule() != null ? request.getSchedule().trim() : "")
                .professorId(professor.getId())
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();

        Course savedCourse = courseRepositoryPort.save(course);
        return mapToDto(savedCourse);
    }

    // Actualiza un curso existente del profesor autenticado
    @Override
    @Transactional
    public CourseResponseDto updateCourse(Long courseId, UpdateCourseRequestDto request, String professorEmail) {
        String email = professorEmail != null ? professorEmail.trim().toLowerCase() : "";
        User professor = userRepositoryPort.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Profesor no encontrado con correo: " + professorEmail));

        Course course = courseRepositoryPort.findByIdAndProfessorId(courseId, professor.getId())
                .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado o no pertenece al profesor"));

        String normalizedCode = request.getCode().trim().toUpperCase();

        if (courseRepositoryPort.existsByCodeAndProfessorIdAndIdNot(normalizedCode, professor.getId(), courseId)) {
            throw new IllegalArgumentException("Ya existe otro curso registrado con el codigo: " + normalizedCode);
        }

        course.setName(request.getName().trim());
        course.setCode(normalizedCode);
        course.setSection(request.getSection().trim().toUpperCase());
        course.setCredits(request.getCredits());
        course.setType(request.getType() != null ? request.getType().trim().toUpperCase() : null);
        course.setSchedule(request.getSchedule() != null ? request.getSchedule().trim() : "");

        Course savedCourse = courseRepositoryPort.save(course);
        return mapToDto(savedCourse);
    }

    // Obtiene la lista de cursos del profesor
    @Override
    @Transactional(readOnly = true)
    public List<CourseResponseDto> getCoursesByProfessor(String professorEmail) {
        String email = professorEmail != null ? professorEmail.trim().toLowerCase() : "";
        User professor = userRepositoryPort.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Profesor no encontrado con correo: " + professorEmail));

        return courseRepositoryPort.findByProfessorId(professor.getId())
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // Mapea la entidad de dominio a DTO de respuesta
    private CourseResponseDto mapToDto(Course course) {
        return CourseResponseDto.builder()
                .id(course.getId())
                .name(course.getName())
                .code(course.getCode())
                .section(course.getSection())
                .credits(course.getCredits())
                .type(course.getType())
                .schedule(course.getSchedule())
                .professorId(course.getProfessorId())
                .active(course.isActive())
                .createdAt(course.getCreatedAt())
                .build();
    }
}
