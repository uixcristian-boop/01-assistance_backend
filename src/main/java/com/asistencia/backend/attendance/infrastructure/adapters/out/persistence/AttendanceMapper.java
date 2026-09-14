package com.asistencia.backend.attendance.infrastructure.adapters.out.persistence;

import com.asistencia.backend.attendance.domain.model.AttendanceRecord;
import com.asistencia.backend.attendance.domain.model.AttendanceSession;
import org.springframework.stereotype.Component;

// Mapeador entre modelos de dominio de asistencia y entidades JPA
@Component
public class AttendanceMapper {

    public AttendanceSessionJpaEntity toSessionJpaEntity(AttendanceSession domain) {
        if (domain == null) return null;
        return AttendanceSessionJpaEntity.builder()
                .id(domain.getId())
                .courseId(domain.getCourseId())
                .sessionDate(domain.getSessionDate())
                .startTime(domain.getStartTime())
                .endTime(domain.getEndTime())
                .toleranceMinutes(domain.getToleranceMinutes())
                .lateThresholdMinutes(domain.getLateThresholdMinutes())
                .sessionToken(domain.getSessionToken())
                .active(domain.isActive())
                .createdAt(domain.getCreatedAt())
                .build();
    }

    public AttendanceSession toSessionDomain(AttendanceSessionJpaEntity entity) {
        if (entity == null) return null;
        return AttendanceSession.builder()
                .id(entity.getId())
                .courseId(entity.getCourseId())
                .sessionDate(entity.getSessionDate())
                .startTime(entity.getStartTime())
                .endTime(entity.getEndTime())
                .toleranceMinutes(entity.getToleranceMinutes())
                .lateThresholdMinutes(entity.getLateThresholdMinutes())
                .sessionToken(entity.getSessionToken())
                .active(entity.isActive())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public AttendanceRecordJpaEntity toRecordJpaEntity(AttendanceRecord domain) {
        if (domain == null) return null;
        return AttendanceRecordJpaEntity.builder()
                .id(domain.getId())
                .sessionId(domain.getSessionId())
                .studentId(domain.getStudentId())
                .status(domain.getStatus())
                .checkInTime(domain.getCheckInTime())
                .confidenceScore(domain.getConfidenceScore())
                .capturePhotoUrl(domain.getCapturePhotoUrl())
                .createdAt(domain.getCreatedAt())
                .build();
    }

    public AttendanceRecord toRecordDomain(AttendanceRecordJpaEntity entity) {
        if (entity == null) return null;
        return AttendanceRecord.builder()
                .id(entity.getId())
                .sessionId(entity.getSessionId())
                .studentId(entity.getStudentId())
                .status(entity.getStatus())
                .checkInTime(entity.getCheckInTime())
                .confidenceScore(entity.getConfidenceScore())
                .capturePhotoUrl(entity.getCapturePhotoUrl())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
