package com.asistencia.backend.course.infrastructure.adapters.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// Repositorio Spring Data JPA para cursos
@Repository
public interface SpringDataCourseRepository extends JpaRepository<CourseJpaEntity, Long> {
    List<CourseJpaEntity> findByProfessorId(Long professorId);
    java.util.Optional<CourseJpaEntity> findByIdAndProfessorId(Long id, Long professorId);
    boolean existsByCodeAndProfessorId(String code, Long professorId);
    boolean existsByCodeAndProfessorIdAndIdNot(String code, Long professorId, Long id);
}
