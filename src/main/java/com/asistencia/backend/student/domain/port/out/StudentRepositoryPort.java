package com.asistencia.backend.student.domain.port.out;

import com.asistencia.backend.student.domain.model.Student;

import java.util.List;
import java.util.Optional;

// Puerto de salida para persistencia de alumnos
public interface StudentRepositoryPort {
    Student save(Student student);
    List<Student> findByCourseId(Long courseId);
    Optional<Student> findByIdAndCourseId(Long id, Long courseId);
    Optional<Student> findById(Long id);
    boolean existsByCodeAndCourseId(String code, Long courseId);
    boolean existsByCodeAndCourseIdAndIdNot(String code, Long courseId, Long id);
    void deleteById(Long id);
}
