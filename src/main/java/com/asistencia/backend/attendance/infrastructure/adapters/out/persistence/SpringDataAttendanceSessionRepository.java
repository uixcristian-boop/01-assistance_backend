package com.asistencia.backend.attendance.infrastructure.adapters.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// Repositorio Spring Data JPA para sesiones de asistencia
@Repository
public interface SpringDataAttendanceSessionRepository extends JpaRepository<AttendanceSessionJpaEntity, Long> {
    Optional<AttendanceSessionJpaEntity> findBySessionToken(String sessionToken);
    List<AttendanceSessionJpaEntity> findByCourseIdOrderByCreatedAtDesc(Long courseId);
    Optional<AttendanceSessionJpaEntity> findFirstByCourseIdAndActiveTrueOrderByCreatedAtDesc(Long courseId);
}
