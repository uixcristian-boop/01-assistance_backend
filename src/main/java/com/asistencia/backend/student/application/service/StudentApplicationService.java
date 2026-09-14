package com.asistencia.backend.student.application.service;

import com.asistencia.backend.auth.domain.model.User;
import com.asistencia.backend.auth.domain.port.out.UserRepositoryPort;
import com.asistencia.backend.course.domain.model.Course;
import com.asistencia.backend.course.domain.port.out.CourseRepositoryPort;
import com.asistencia.backend.student.application.dto.CreateStudentRequestDto;
import com.asistencia.backend.student.application.dto.SaveFaceDescriptorRequestDto;
import com.asistencia.backend.student.application.dto.StudentResponseDto;
import com.asistencia.backend.student.application.dto.UpdateStudentRequestDto;
import com.asistencia.backend.student.domain.model.Student;
import com.asistencia.backend.student.domain.port.in.StudentUseCase;
import com.asistencia.backend.student.domain.port.out.StudentRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

// Servicio de aplicacion para la logica de negocio de alumnos y enrolamiento facial
@Service
@RequiredArgsConstructor
public class StudentApplicationService implements StudentUseCase {

    private final StudentRepositoryPort studentRepositoryPort;
    private final CourseRepositoryPort courseRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;

    // Registra un nuevo alumno en un curso
    @Override
    @Transactional
    public StudentResponseDto createStudent(Long courseId, CreateStudentRequestDto request, String professorEmail) {
        verifyCourseOwnership(courseId, professorEmail);

        String normalizedCode = request.getCode().trim().toUpperCase();

        if (studentRepositoryPort.existsByCodeAndCourseId(normalizedCode, courseId)) {
            throw new IllegalArgumentException("Ya existe un alumno registrado con el codigo: " + normalizedCode + " en este curso");
        }

        Student student = Student.builder()
                .courseId(courseId)
                .code(normalizedCode)
                .firstName(request.getFirstName().trim())
                .lastName(request.getLastName().trim())
                .email(request.getEmail() != null ? request.getEmail().trim().toLowerCase() : null)
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();

        Student saved = studentRepositoryPort.save(student);
        return mapToDto(saved);
    }

    // Actualiza los datos de un alumno
    @Override
    @Transactional
    public StudentResponseDto updateStudent(Long courseId, Long studentId, UpdateStudentRequestDto request, String professorEmail) {
        verifyCourseOwnership(courseId, professorEmail);

        Student student = studentRepositoryPort.findByIdAndCourseId(studentId, courseId)
                .orElseThrow(() -> new IllegalArgumentException("Alumno no encontrado en este curso"));

        String normalizedCode = request.getCode().trim().toUpperCase();

        if (studentRepositoryPort.existsByCodeAndCourseIdAndIdNot(normalizedCode, courseId, studentId)) {
            throw new IllegalArgumentException("Ya existe otro alumno registrado con el codigo: " + normalizedCode + " en este curso");
        }

        student.setCode(normalizedCode);
        student.setFirstName(request.getFirstName().trim());
        student.setLastName(request.getLastName().trim());
        student.setEmail(request.getEmail() != null ? request.getEmail().trim().toLowerCase() : null);

        Student saved = studentRepositoryPort.save(student);
        return mapToDto(saved);
    }

    // Lista todos los alumnos inscritos en un curso
    @Override
    @Transactional(readOnly = true)
    public List<StudentResponseDto> getStudentsByCourse(Long courseId, String professorEmail) {
        verifyCourseOwnership(courseId, professorEmail);

        return studentRepositoryPort.findByCourseId(courseId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // Obtiene un alumno por id
    @Override
    @Transactional(readOnly = true)
    public StudentResponseDto getStudentById(Long courseId, Long studentId, String professorEmail) {
        verifyCourseOwnership(courseId, professorEmail);

        Student student = studentRepositoryPort.findByIdAndCourseId(studentId, courseId)
                .orElseThrow(() -> new IllegalArgumentException("Alumno no encontrado"));

        return mapToDto(student);
    }

    // Guarda el vector biometrico facial y foto de referencia del alumno
    @Override
    @Transactional
    public StudentResponseDto saveFaceDescriptor(Long courseId, Long studentId, SaveFaceDescriptorRequestDto request, String professorEmail) {
        verifyCourseOwnership(courseId, professorEmail);

        Student student = studentRepositoryPort.findByIdAndCourseId(studentId, courseId)
                .orElseThrow(() -> new IllegalArgumentException("Alumno no encontrado en este curso"));

        student.setFaceDescriptor(request.getFaceDescriptor().trim());
        if (request.getPhotoUrl() != null && !request.getPhotoUrl().trim().isEmpty()) {
            student.setPhotoUrl(request.getPhotoUrl().trim());
        }

        Student saved = studentRepositoryPort.save(student);
        return mapToDto(saved);
    }

    // Elimina un alumno del curso
    @Override
    @Transactional
    public void deleteStudent(Long courseId, Long studentId, String professorEmail) {
        verifyCourseOwnership(courseId, professorEmail);

        Student student = studentRepositoryPort.findByIdAndCourseId(studentId, courseId)
                .orElseThrow(() -> new IllegalArgumentException("Alumno no encontrado en este curso"));

        studentRepositoryPort.deleteById(student.getId());
    }

    // Valida que el curso pertenezca al profesor autenticado
    private Course verifyCourseOwnership(Long courseId, String professorEmail) {
        String email = professorEmail != null ? professorEmail.trim().toLowerCase() : "";
        User professor = userRepositoryPort.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Profesor no autenticado correctamente"));

        return courseRepositoryPort.findByIdAndProfessorId(courseId, professor.getId())
                .orElseThrow(() -> new IllegalArgumentException("El curso no pertenece al profesor o no existe"));
    }

    // Convierte modelo a DTO
    private StudentResponseDto mapToDto(Student student) {
        boolean hasFace = student.getFaceDescriptor() != null && !student.getFaceDescriptor().trim().isEmpty();
        return StudentResponseDto.builder()
                .id(student.getId())
                .courseId(student.getCourseId())
                .code(student.getCode())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .email(student.getEmail())
                .hasFaceDescriptor(hasFace)
                .faceDescriptor(student.getFaceDescriptor())
                .photoUrl(student.getPhotoUrl())
                .active(student.isActive())
                .createdAt(student.getCreatedAt())
                .build();
    }
}
