package com.asistencia.backend.attendance.domain.port.out;

import com.asistencia.backend.attendance.domain.model.AttendanceRecord;
import com.asistencia.backend.attendance.domain.model.AttendanceSession;

import java.util.List;
import java.util.Optional;

// Puerto de salida para persistencia de sesiones y registros de asistencia
public interface AttendanceRepositoryPort {
    AttendanceSession saveSession(AttendanceSession session);
    Optional<AttendanceSession> findSessionById(Long id);
    Optional<AttendanceSession> findSessionByToken(String token);
    List<AttendanceSession> findSessionsByCourseId(Long courseId);
    Optional<AttendanceSession> findActiveSessionByCourseId(Long courseId);

    AttendanceRecord saveRecord(AttendanceRecord record);
    List<AttendanceRecord> findRecordsBySessionId(Long sessionId);
    Optional<AttendanceRecord> findRecordBySessionIdAndStudentId(Long sessionId, Long studentId);
    void deleteSessionById(Long sessionId);
}
