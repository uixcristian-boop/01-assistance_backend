package com.asistencia.backend.course.infrastructure.adapters.out.persistence;

import com.asistencia.backend.course.domain.model.Course;
import org.springframework.stereotype.Component;

// Mapeador entre la entidad de dominio Course y la entidad JPA
@Component
public class CourseMapper {

    public CourseJpaEntity toJpaEntity(Course domain) {
        if (domain == null) return null;
        return CourseJpaEntity.builder()
                .id(domain.getId())
                .name(domain.getName())
                .code(domain.getCode())
                .section(domain.getSection())
                .credits(domain.getCredits())
                .type(domain.getType())
                .schedule(domain.getSchedule())
                .professorId(domain.getProfessorId())
                .active(domain.isActive())
                .createdAt(domain.getCreatedAt())
                .build();
    }

    public Course toDomain(CourseJpaEntity entity) {
        if (entity == null) return null;
        return Course.builder()
                .id(entity.getId())
                .name(entity.getName())
                .code(entity.getCode())
                .section(entity.getSection())
                .credits(entity.getCredits())
                .type(entity.getType())
                .schedule(entity.getSchedule())
                .professorId(entity.getProfessorId())
                .active(entity.isActive())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
