package com.asistencia.backend.student.infrastructure.adapters.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// Repositorio Spring Data JPA para alumnos
@Repository
public interface SpringDataStudentRepository extends JpaRepository<StudentJpaEntity, Long> {
    List<StudentJpaEntity> findByCourseId(Long courseId);
    List<StudentJpaEntity> findByCourseIdOrderByLastNameAscFirstNameAsc(Long courseId);
    Optional<StudentJpaEntity> findByIdAndCourseId(Long id, Long courseId);
    boolean existsByCodeAndCourseId(String code, Long courseId);
    boolean existsByCodeAndCourseIdAndIdNot(String code, Long courseId, Long id);
}
