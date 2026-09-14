package com.asistencia.backend.attendance.infrastructure.adapters.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// Repositorio Spring Data JPA para registros de asistencia
@Repository
public interface SpringDataAttendanceRecordRepository extends JpaRepository<AttendanceRecordJpaEntity, Long> {
    List<AttendanceRecordJpaEntity> findBySessionIdOrderByCheckInTimeAsc(Long sessionId);
    Optional<AttendanceRecordJpaEntity> findBySessionIdAndStudentId(Long sessionId, Long studentId);
    void deleteBySessionId(Long sessionId);
}
