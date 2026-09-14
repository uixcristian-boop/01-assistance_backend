package com.asistencia.backend.attendance.domain.port.in;

import com.asistencia.backend.attendance.application.dto.AttendanceRecordResponseDto;
import com.asistencia.backend.attendance.application.dto.AttendanceSessionResponseDto;
import com.asistencia.backend.attendance.application.dto.CheckInRequestDto;
import com.asistencia.backend.attendance.application.dto.CreateAttendanceSessionRequestDto;
import com.asistencia.backend.attendance.application.dto.PublicSessionResponseDto;

import java.util.List;

// Caso de uso para sesiones de asistencia, generacion de QR y marcaje facial
public interface AttendanceUseCase {
    AttendanceSessionResponseDto createSession(Long courseId, CreateAttendanceSessionRequestDto request, String professorEmail);
    AttendanceSessionResponseDto getActiveSession(Long courseId, String professorEmail);
    AttendanceSessionResponseDto closeSession(Long courseId, Long sessionId, String professorEmail);
    AttendanceSessionResponseDto getSessionDetails(Long courseId, Long sessionId, String professorEmail);
    List<AttendanceSessionResponseDto> getSessionsByCourse(Long courseId, String professorEmail);
    void deleteSession(Long courseId, Long sessionId, String professorEmail);

    PublicSessionResponseDto getPublicSession(String sessionToken);
    AttendanceRecordResponseDto checkInAttendance(String sessionToken, CheckInRequestDto request);
}
