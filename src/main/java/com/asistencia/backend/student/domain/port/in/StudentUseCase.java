package com.asistencia.backend.student.domain.port.in;

import com.asistencia.backend.student.application.dto.CreateStudentRequestDto;
import com.asistencia.backend.student.application.dto.SaveFaceDescriptorRequestDto;
import com.asistencia.backend.student.application.dto.StudentResponseDto;
import com.asistencia.backend.student.application.dto.UpdateStudentRequestDto;

import java.util.List;

// Caso de uso para la gestion de alumnos y enrolamiento biometrico
public interface StudentUseCase {
    StudentResponseDto createStudent(Long courseId, CreateStudentRequestDto request, String professorEmail);
    StudentResponseDto updateStudent(Long courseId, Long studentId, UpdateStudentRequestDto request, String professorEmail);
    List<StudentResponseDto> getStudentsByCourse(Long courseId, String professorEmail);
    StudentResponseDto getStudentById(Long courseId, Long studentId, String professorEmail);
    StudentResponseDto saveFaceDescriptor(Long courseId, Long studentId, SaveFaceDescriptorRequestDto request, String professorEmail);
    void deleteStudent(Long courseId, Long studentId, String professorEmail);
}
