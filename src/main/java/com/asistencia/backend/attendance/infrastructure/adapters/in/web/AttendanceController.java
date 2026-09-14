package com.asistencia.backend.attendance.infrastructure.adapters.in.web;

import com.asistencia.backend.attendance.application.dto.AttendanceRecordResponseDto;
import com.asistencia.backend.attendance.application.dto.AttendanceSessionResponseDto;
import com.asistencia.backend.attendance.application.dto.CheckInRequestDto;
import com.asistencia.backend.attendance.application.dto.CreateAttendanceSessionRequestDto;
import com.asistencia.backend.attendance.application.dto.PublicSessionResponseDto;
import com.asistencia.backend.attendance.domain.port.in.AttendanceUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

// Controlador REST para gestion de sesiones de asistencia y marcaje de alumnos
@RestController
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceUseCase attendanceUseCase;

    // Inicia una sesion de clase con QR para el curso (Docente)
    @PostMapping("/api/courses/{courseId}/attendance/sessions")
    public ResponseEntity<AttendanceSessionResponseDto> createSession(
            @PathVariable Long courseId,
            @Valid @RequestBody CreateAttendanceSessionRequestDto request,
            Principal principal
    ) {
        AttendanceSessionResponseDto session = attendanceUseCase.createSession(courseId, request, principal.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(session);
    }

    // Obtiene la sesion activa actual del curso (Docente)
    @GetMapping("/api/courses/{courseId}/attendance/active")
    public ResponseEntity<AttendanceSessionResponseDto> getActiveSession(
            @PathVariable Long courseId,
            Principal principal
    ) {
        AttendanceSessionResponseDto session = attendanceUseCase.getActiveSession(courseId, principal.getName());
        return ResponseEntity.ok(session);
    }

    // Cierra una sesion de asistencia activa (Docente)
    @PutMapping("/api/courses/{courseId}/attendance/sessions/{sessionId}/close")
    public ResponseEntity<AttendanceSessionResponseDto> closeSession(
            @PathVariable Long courseId,
            @PathVariable Long sessionId,
            Principal principal
    ) {
        AttendanceSessionResponseDto closed = attendanceUseCase.closeSession(courseId, sessionId, principal.getName());
        return ResponseEntity.ok(closed);
    }

    // Obtiene el detalle y lista de asistencia de una sesion (Docente)
    @GetMapping("/api/courses/{courseId}/attendance/sessions/{sessionId}")
    public ResponseEntity<AttendanceSessionResponseDto> getSessionDetails(
            @PathVariable Long courseId,
            @PathVariable Long sessionId,
            Principal principal
    ) {
        AttendanceSessionResponseDto session = attendanceUseCase.getSessionDetails(courseId, sessionId, principal.getName());
        return ResponseEntity.ok(session);
    }

    // Obtiene el historial de sesiones de asistencia de un curso (Docente)
    @GetMapping("/api/courses/{courseId}/attendance/sessions")
    public ResponseEntity<List<AttendanceSessionResponseDto>> getSessionsByCourse(
            @PathVariable Long courseId,
            Principal principal
    ) {
        List<AttendanceSessionResponseDto> sessions = attendanceUseCase.getSessionsByCourse(courseId, principal.getName());
        return ResponseEntity.ok(sessions);
    }

    // Elimina una sesion de asistencia del historial (Docente)
    @DeleteMapping("/api/courses/{courseId}/attendance/sessions/{sessionId}")
    public ResponseEntity<Void> deleteSession(
            @PathVariable Long courseId,
            @PathVariable Long sessionId,
            Principal principal
    ) {
        attendanceUseCase.deleteSession(courseId, sessionId, principal.getName());
        return ResponseEntity.noContent().build();
    }

    // Consulta publica de datos de la sesion por el token del QR (Alumno)
    @GetMapping("/api/attendance/public/sessions/{token}")
    public ResponseEntity<PublicSessionResponseDto> getPublicSession(
            @PathVariable String token
    ) {
        PublicSessionResponseDto session = attendanceUseCase.getPublicSession(token);
        return ResponseEntity.ok(session);
    }

    // Registro publico de asistencia facial mediante el token de QR (Alumno)
    @PostMapping("/api/attendance/public/sessions/{token}/check-in")
    public ResponseEntity<AttendanceRecordResponseDto> checkIn(
            @PathVariable String token,
            @Valid @RequestBody CheckInRequestDto request
    ) {
        AttendanceRecordResponseDto record = attendanceUseCase.checkInAttendance(token, request);
        return ResponseEntity.ok(record);
    }
}
