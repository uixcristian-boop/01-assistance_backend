package com.asistencia.backend.attendance.infrastructure.adapters.out.persistence;

import com.asistencia.backend.attendance.domain.model.AttendanceRecord;
import com.asistencia.backend.attendance.domain.model.AttendanceSession;
import com.asistencia.backend.attendance.domain.port.out.AttendanceRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// Adaptador de persistencia que implementa AttendanceRepositoryPort
@Component
@RequiredArgsConstructor
public class AttendanceRepositoryAdapter implements AttendanceRepositoryPort {

    private final SpringDataAttendanceSessionRepository sessionRepository;
    private final SpringDataAttendanceRecordRepository recordRepository;
    private final AttendanceMapper attendanceMapper;

    @Override
    public AttendanceSession saveSession(AttendanceSession session) {
        AttendanceSessionJpaEntity jpaEntity = attendanceMapper.toSessionJpaEntity(session);
        AttendanceSessionJpaEntity saved = sessionRepository.save(jpaEntity);
        return attendanceMapper.toSessionDomain(saved);
    }

    @Override
    public Optional<AttendanceSession> findSessionById(Long id) {
        return sessionRepository.findById(id)
                .map(attendanceMapper::toSessionDomain);
    }

    @Override
    public Optional<AttendanceSession> findSessionByToken(String token) {
        return sessionRepository.findBySessionToken(token)
                .map(attendanceMapper::toSessionDomain);
    }

    @Override
    public List<AttendanceSession> findSessionsByCourseId(Long courseId) {
        return sessionRepository.findByCourseIdOrderByCreatedAtDesc(courseId)
                .stream()
                .map(attendanceMapper::toSessionDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<AttendanceSession> findActiveSessionByCourseId(Long courseId) {
        return sessionRepository.findFirstByCourseIdAndActiveTrueOrderByCreatedAtDesc(courseId)
                .map(attendanceMapper::toSessionDomain);
    }

    @Override
    public AttendanceRecord saveRecord(AttendanceRecord record) {
        AttendanceRecordJpaEntity jpaEntity = attendanceMapper.toRecordJpaEntity(record);
        AttendanceRecordJpaEntity saved = recordRepository.save(jpaEntity);
        return attendanceMapper.toRecordDomain(saved);
    }

    @Override
    public List<AttendanceRecord> findRecordsBySessionId(Long sessionId) {
        return recordRepository.findBySessionIdOrderByCheckInTimeAsc(sessionId)
                .stream()
                .map(attendanceMapper::toRecordDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<AttendanceRecord> findRecordBySessionIdAndStudentId(Long sessionId, Long studentId) {
        return recordRepository.findBySessionIdAndStudentId(sessionId, studentId)
                .map(attendanceMapper::toRecordDomain);
    }

    @Override
    public void deleteSessionById(Long sessionId) {
        recordRepository.deleteBySessionId(sessionId);
        sessionRepository.deleteById(sessionId);
    }
}
