package com.asistencia.backend.student.infrastructure.adapters.out.persistence;

import com.asistencia.backend.student.domain.model.Student;
import org.springframework.stereotype.Component;

// Mapeador entre la entidad de dominio Student y la entidad JPA
@Component
public class StudentMapper {

    public StudentJpaEntity toJpaEntity(Student domain) {
        if (domain == null) return null;
        return StudentJpaEntity.builder()
                .id(domain.getId())
                .courseId(domain.getCourseId())
                .code(domain.getCode())
                .firstName(domain.getFirstName())
                .lastName(domain.getLastName())
                .email(domain.getEmail())
                .faceDescriptor(domain.getFaceDescriptor())
                .photoUrl(domain.getPhotoUrl())
                .active(domain.isActive())
                .createdAt(domain.getCreatedAt())
                .build();
    }

    public Student toDomain(StudentJpaEntity entity) {
        if (entity == null) return null;
        return Student.builder()
                .id(entity.getId())
                .courseId(entity.getCourseId())
                .code(entity.getCode())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .faceDescriptor(entity.getFaceDescriptor())
                .photoUrl(entity.getPhotoUrl())
                .active(entity.isActive())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
