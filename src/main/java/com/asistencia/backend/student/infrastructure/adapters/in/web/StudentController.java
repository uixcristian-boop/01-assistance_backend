package com.asistencia.backend.student.infrastructure.adapters.in.web;

import com.asistencia.backend.student.application.dto.CreateStudentRequestDto;
import com.asistencia.backend.student.application.dto.SaveFaceDescriptorRequestDto;
import com.asistencia.backend.student.application.dto.StudentResponseDto;
import com.asistencia.backend.student.application.dto.UpdateStudentRequestDto;
import com.asistencia.backend.student.domain.port.in.StudentUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

// Controlador REST para la administracion de alumnos por curso
@RestController
@RequestMapping("/api/courses/{courseId}/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentUseCase studentUseCase;

    // Registra un nuevo alumno en el curso
    @PostMapping
    public ResponseEntity<StudentResponseDto> createStudent(
            @PathVariable Long courseId,
            @Valid @RequestBody CreateStudentRequestDto request,
            Principal principal
    ) {
        StudentResponseDto created = studentUseCase.createStudent(courseId, request, principal.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // Actualiza los datos de un alumno
    @PutMapping("/{studentId}")
    public ResponseEntity<StudentResponseDto> updateStudent(
            @PathVariable Long courseId,
            @PathVariable Long studentId,
            @Valid @RequestBody UpdateStudentRequestDto request,
            Principal principal
    ) {
        StudentResponseDto updated = studentUseCase.updateStudent(courseId, studentId, request, principal.getName());
        return ResponseEntity.ok(updated);
    }

    // Lista todos los alumnos inscritos en el curso
    @GetMapping
    public ResponseEntity<List<StudentResponseDto>> getStudents(
            @PathVariable Long courseId,
            Principal principal
    ) {
        List<StudentResponseDto> students = studentUseCase.getStudentsByCourse(courseId, principal.getName());
        return ResponseEntity.ok(students);
    }

    // Obtiene un alumno especifico
    @GetMapping("/{studentId}")
    public ResponseEntity<StudentResponseDto> getStudent(
            @PathVariable Long courseId,
            @PathVariable Long studentId,
            Principal principal
    ) {
        StudentResponseDto student = studentUseCase.getStudentById(courseId, studentId, principal.getName());
        return ResponseEntity.ok(student);
    }

    // Guarda el vector biometrico facial capturado por la camara
    @PutMapping("/{studentId}/face-descriptor")
    public ResponseEntity<StudentResponseDto> saveFaceDescriptor(
            @PathVariable Long courseId,
            @PathVariable Long studentId,
            @Valid @RequestBody SaveFaceDescriptorRequestDto request,
            Principal principal
    ) {
        StudentResponseDto updated = studentUseCase.saveFaceDescriptor(courseId, studentId, request, principal.getName());
        return ResponseEntity.ok(updated);
    }

    // Elimina un alumno del curso
    @DeleteMapping("/{studentId}")
    public ResponseEntity<Void> deleteStudent(
            @PathVariable Long courseId,
            @PathVariable Long studentId,
            Principal principal
    ) {
        studentUseCase.deleteStudent(courseId, studentId, principal.getName());
        return ResponseEntity.noContent().build();
    }
}
