package com.asistencia.backend.course.domain.port.out;

import com.asistencia.backend.course.domain.model.Course;

import java.util.List;
import java.util.Optional;

// Puerto de salida para persistencia de cursos
public interface CourseRepositoryPort {
    Course save(Course course);
    List<Course> findByProfessorId(Long professorId);
    Optional<Course> findById(Long id);
    Optional<Course> findByIdAndProfessorId(Long id, Long professorId);
    boolean existsByCodeAndProfessorId(String code, Long professorId);
    boolean existsByCodeAndProfessorIdAndIdNot(String code, Long professorId, Long id);
}
