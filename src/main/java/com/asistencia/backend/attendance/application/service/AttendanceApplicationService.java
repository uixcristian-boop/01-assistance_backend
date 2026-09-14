package com.asistencia.backend.attendance.application.service;

import com.asistencia.backend.attendance.application.dto.AttendanceRecordResponseDto;
import com.asistencia.backend.attendance.application.dto.AttendanceSessionResponseDto;
import com.asistencia.backend.attendance.application.dto.CheckInRequestDto;
import com.asistencia.backend.attendance.application.dto.CreateAttendanceSessionRequestDto;
import com.asistencia.backend.attendance.application.dto.PublicSessionResponseDto;
import com.asistencia.backend.attendance.domain.model.AttendanceRecord;
import com.asistencia.backend.attendance.domain.model.AttendanceSession;
import com.asistencia.backend.attendance.domain.model.AttendanceStatus;
import com.asistencia.backend.attendance.domain.port.in.AttendanceUseCase;
import com.asistencia.backend.attendance.domain.port.out.AttendanceRepositoryPort;
import com.asistencia.backend.auth.domain.model.User;
import com.asistencia.backend.auth.domain.port.out.UserRepositoryPort;
import com.asistencia.backend.course.domain.model.Course;
import com.asistencia.backend.course.domain.port.out.CourseRepositoryPort;
import com.asistencia.backend.student.application.dto.StudentResponseDto;
import com.asistencia.backend.student.domain.model.Student;
import com.asistencia.backend.student.domain.port.out.StudentRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

// Servicio de aplicacion para la gestion de sesiones y validacion de asistencia
@Service
@RequiredArgsConstructor
public class AttendanceApplicationService implements AttendanceUseCase {

    private final AttendanceRepositoryPort attendanceRepositoryPort;
    private final CourseRepositoryPort courseRepositoryPort;
    private final StudentRepositoryPort studentRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;

    // Inicia una nueva sesion de asistencia generando un token unico de QR
    @Override
    @Transactional
    public AttendanceSessionResponseDto createSession(Long courseId, CreateAttendanceSessionRequestDto request, String professorEmail) {
        Course course = verifyCourseOwnership(courseId, professorEmail);

        // Desactiva cualquier sesion previa abierta para el curso
        attendanceRepositoryPort.findActiveSessionByCourseId(courseId).ifPresent(prev -> {
            prev.setActive(false);
            attendanceRepositoryPort.saveSession(prev);
        });

        int tolerance = request.getToleranceMinutes() != null && request.getToleranceMinutes() > 0
                ? request.getToleranceMinutes() : 15;
        int lateThreshold = request.getLateThresholdMinutes() != null && request.getLateThresholdMinutes() > 0
                ? request.getLateThresholdMinutes() : 30;

        String sessionToken = UUID.randomUUID().toString().replace("-", "").substring(0, 16);

        AttendanceSession session = AttendanceSession.builder()
                .courseId(courseId)
                .sessionDate(LocalDate.now())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .toleranceMinutes(tolerance)
                .lateThresholdMinutes(lateThreshold)
                .sessionToken(sessionToken)
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();

        AttendanceSession saved = attendanceRepositoryPort.saveSession(session);
        return mapSessionToDto(saved, course);
    }

    // Obtiene la sesion activa actual del curso
    @Override
    @Transactional(readOnly = true)
    public AttendanceSessionResponseDto getActiveSession(Long courseId, String professorEmail) {
        Course course = verifyCourseOwnership(courseId, professorEmail);

        Optional<AttendanceSession> activeSession = attendanceRepositoryPort.findActiveSessionByCourseId(courseId);
        return activeSession.map(session -> mapSessionToDto(session, course)).orElse(null);
    }

    // Cierra manualmente una sesion de asistencia activa
    @Override
    @Transactional
    public AttendanceSessionResponseDto closeSession(Long courseId, Long sessionId, String professorEmail) {
        Course course = verifyCourseOwnership(courseId, professorEmail);

        AttendanceSession session = attendanceRepositoryPort.findSessionById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Sesion no encontrada"));

        if (!session.getCourseId().equals(courseId)) {
            throw new IllegalArgumentException("La sesion no corresponde a este curso");
        }

        session.setActive(false);
        AttendanceSession saved = attendanceRepositoryPort.saveSession(session);
        return mapSessionToDto(saved, course);
    }

    // Obtiene el detalle completo de una sesion con su lista de asistencia
    @Override
    @Transactional(readOnly = true)
    public AttendanceSessionResponseDto getSessionDetails(Long courseId, Long sessionId, String professorEmail) {
        Course course = verifyCourseOwnership(courseId, professorEmail);

        AttendanceSession session = attendanceRepositoryPort.findSessionById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Sesion no encontrada"));

        return mapSessionToDto(session, course);
    }

    // Obtiene el historial de sesiones de un curso
    @Override
    @Transactional(readOnly = true)
    public List<AttendanceSessionResponseDto> getSessionsByCourse(Long courseId, String professorEmail) {
        Course course = verifyCourseOwnership(courseId, professorEmail);

        return attendanceRepositoryPort.findSessionsByCourseId(courseId)
                .stream()
                .map(session -> mapSessionToDto(session, course))
                .collect(Collectors.toList());
    }

    // Elimina una sesion de asistencia del historial
    @Override
    @Transactional
    public void deleteSession(Long courseId, Long sessionId, String professorEmail) {
        verifyCourseOwnership(courseId, professorEmail);
        AttendanceSession session = attendanceRepositoryPort.findSessionById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Sesion no encontrada"));

        if (!session.getCourseId().equals(courseId)) {
            throw new IllegalArgumentException("La sesion no corresponde a este curso");
        }

        attendanceRepositoryPort.deleteSessionById(sessionId);
    }

    // Retorna la informacion publica de la sesion para el escaneo del alumno
    @Override
    @Transactional(readOnly = true)
    public PublicSessionResponseDto getPublicSession(String sessionToken) {
        AttendanceSession session = attendanceRepositoryPort.findSessionByToken(sessionToken)
                .orElseThrow(() -> new IllegalArgumentException("Sesion de asistencia no valida o expirada"));

        Course course = courseRepositoryPort.findById(session.getCourseId())
                .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado"));

        List<Student> students = studentRepositoryPort.findByCourseId(session.getCourseId());
        List<StudentResponseDto> studentDtos = students.stream()
                .filter(Student::isActive)
                .map(s -> StudentResponseDto.builder()
                        .id(s.getId())
                        .courseId(s.getCourseId())
                        .code(s.getCode())
                        .firstName(s.getFirstName())
                        .lastName(s.getLastName())
                        .email(s.getEmail())
                        .hasFaceDescriptor(s.getFaceDescriptor() != null && !s.getFaceDescriptor().trim().isEmpty())
                        .faceDescriptor(s.getFaceDescriptor())
                        .photoUrl(s.getPhotoUrl())
                        .active(s.isActive())
                        .build())
                .collect(Collectors.toList());

        return PublicSessionResponseDto.builder()
                .sessionToken(session.getSessionToken())
                .courseName(course.getName())
                .courseCode(course.getCode())
                .section(course.getSection())
                .sessionDate(session.getSessionDate())
                .startTime(session.getStartTime())
                .endTime(session.getEndTime())
                .toleranceMinutes(session.getToleranceMinutes())
                .lateThresholdMinutes(session.getLateThresholdMinutes())
                .active(session.isActive())
                .students(studentDtos)
                .build();
    }

    // Registra la asistencia facial del alumno evaluando horario y tolerancia
    @Override
    @Transactional
    public AttendanceRecordResponseDto checkInAttendance(String sessionToken, CheckInRequestDto request) {
        AttendanceSession session = attendanceRepositoryPort.findSessionByToken(sessionToken)
                .orElseThrow(() -> new IllegalArgumentException("Sesion de asistencia no valida o inexistente"));

        if (!session.isActive()) {
            throw new IllegalArgumentException("La sesion de asistencia ya ha sido cerrada");
        }

        Student student = studentRepositoryPort.findByIdAndCourseId(request.getStudentId(), session.getCourseId())
                .orElseThrow(() -> new IllegalArgumentException("El alumno no pertenece a este curso"));

        // Si ya marco asistencia en esta sesion, devuelve el registro existente
        Optional<AttendanceRecord> existingRecord = attendanceRepositoryPort
                .findRecordBySessionIdAndStudentId(session.getId(), student.getId());
        if (existingRecord.isPresent()) {
            return mapRecordToDto(existingRecord.get(), student);
        }

        LocalTime now = LocalTime.now();
        LocalTime classStart = session.getStartTime();
        LocalTime classEnd = session.getEndTime();

        // Validacion de limite de horario de clase
        if (now.isAfter(classEnd)) {
            throw new IllegalArgumentException("El horario de la clase ya ha culminado (" + classEnd + ")");
        }

        // Calculo de tiempo transcurrido desde el inicio de la clase
        long minutesFromStart = 0;
        if (now.isAfter(classStart)) {
            minutesFromStart = Duration.between(classStart, now).toMinutes();
        }

        AttendanceStatus computedStatus;
        if (minutesFromStart <= session.getToleranceMinutes()) {
            computedStatus = AttendanceStatus.PRESENTE;
        } else if (minutesFromStart <= session.getLateThresholdMinutes()) {
            computedStatus = AttendanceStatus.TARDANZA;
        } else {
            // Pasado el umbral maximo de tardanza
            computedStatus = AttendanceStatus.TARDANZA;
        }

        AttendanceRecord record = AttendanceRecord.builder()
                .sessionId(session.getId())
                .studentId(student.getId())
                .status(computedStatus)
                .checkInTime(LocalDateTime.now())
                .confidenceScore(request.getConfidenceScore())
                .capturePhotoUrl(request.getCapturePhotoUrl())
                .createdAt(LocalDateTime.now())
                .build();

        AttendanceRecord saved = attendanceRepositoryPort.saveRecord(record);
        return mapRecordToDto(saved, student);
    }

    // Valida la titularidad del curso por el profesor
    private Course verifyCourseOwnership(Long courseId, String professorEmail) {
        String email = professorEmail != null ? professorEmail.trim().toLowerCase() : "";
        User professor = userRepositoryPort.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Profesor no autenticado"));

        return courseRepositoryPort.findByIdAndProfessorId(courseId, professor.getId())
                .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado o no pertenece al profesor"));
    }

    // Mapea la sesion y sus registros con resumen de conteos
    private AttendanceSessionResponseDto mapSessionToDto(AttendanceSession session, Course course) {
        List<Student> students = studentRepositoryPort.findByCourseId(session.getCourseId());
        List<AttendanceRecord> records = attendanceRepositoryPort.findRecordsBySessionId(session.getId());

        Map<Long, Student> studentMap = students.stream()
                .collect(Collectors.toMap(Student::getId, Function.identity()));

        int present = 0;
        int late = 0;

        List<AttendanceRecordResponseDto> recordDtos = new ArrayList<>();
        for (AttendanceRecord record : records) {
            Student st = studentMap.get(record.getStudentId());
            if (record.getStatus() == AttendanceStatus.PRESENTE) present++;
            if (record.getStatus() == AttendanceStatus.TARDANZA) late++;
            recordDtos.add(mapRecordToDto(record, st));
        }

        int absent = Math.max(0, students.size() - recordDtos.size());

        return AttendanceSessionResponseDto.builder()
                .id(session.getId())
                .courseId(session.getCourseId())
                .courseName(course.getName())
                .courseCode(course.getCode())
                .sessionDate(session.getSessionDate())
                .startTime(session.getStartTime())
                .endTime(session.getEndTime())
                .toleranceMinutes(session.getToleranceMinutes())
                .lateThresholdMinutes(session.getLateThresholdMinutes())
                .sessionToken(session.getSessionToken())
                .active(session.isActive())
                .totalStudents(students.size())
                .presentCount(present)
                .lateCount(late)
                .absentCount(absent)
                .records(recordDtos)
                .createdAt(session.getCreatedAt())
                .build();
    }

    // Mapea un registro individual de asistencia
    private AttendanceRecordResponseDto mapRecordToDto(AttendanceRecord record, Student student) {
        String code = student != null ? student.getCode() : "";
        String name = student != null ? (student.getLastName() + " " + student.getFirstName()).trim() : "Alumno";

        return AttendanceRecordResponseDto.builder()
                .id(record.getId())
                .sessionId(record.getSessionId())
                .studentId(record.getStudentId())
                .studentCode(code)
                .studentName(name)
                .status(record.getStatus())
                .checkInTime(record.getCheckInTime())
                .confidenceScore(record.getConfidenceScore())
                .capturePhotoUrl(record.getCapturePhotoUrl())
                .createdAt(record.getCreatedAt())
                .build();
    }
}
