package com.asistencia.backend.course.infrastructure.adapters.out.persistence;

import com.asistencia.backend.course.domain.model.Course;
import com.asistencia.backend.course.domain.port.out.CourseRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// Adaptador de persistencia que implementa CourseRepositoryPort
@Component
@RequiredArgsConstructor
public class CourseRepositoryAdapter implements CourseRepositoryPort {

    private final SpringDataCourseRepository springDataCourseRepository;
    private final CourseMapper courseMapper;

    @Override
    public Course save(Course course) {
        CourseJpaEntity jpaEntity = courseMapper.toJpaEntity(course);
        CourseJpaEntity saved = springDataCourseRepository.save(jpaEntity);
        return courseMapper.toDomain(saved);
    }

    @Override
    public List<Course> findByProfessorId(Long professorId) {
        return springDataCourseRepository.findByProfessorId(professorId)
                .stream()
                .map(courseMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Course> findById(Long id) {
        return springDataCourseRepository.findById(id)
                .map(courseMapper::toDomain);
    }

    @Override
    public Optional<Course> findByIdAndProfessorId(Long id, Long professorId) {
        return springDataCourseRepository.findByIdAndProfessorId(id, professorId)
                .map(courseMapper::toDomain);
    }

    @Override
    public boolean existsByCodeAndProfessorId(String code, Long professorId) {
        return springDataCourseRepository.existsByCodeAndProfessorId(code, professorId);
    }

    @Override
    public boolean existsByCodeAndProfessorIdAndIdNot(String code, Long professorId, Long id) {
        return springDataCourseRepository.existsByCodeAndProfessorIdAndIdNot(code, professorId, id);
    }
}
