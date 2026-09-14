package com.asistencia.backend.student.infrastructure.adapters.out.persistence;

import com.asistencia.backend.student.domain.model.Student;
import com.asistencia.backend.student.domain.port.out.StudentRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// Adaptador de persistencia que implementa StudentRepositoryPort
@Component
@RequiredArgsConstructor
public class StudentRepositoryAdapter implements StudentRepositoryPort {

    private final SpringDataStudentRepository springDataStudentRepository;
    private final StudentMapper studentMapper;

    @Override
    public Student save(Student student) {
        StudentJpaEntity jpaEntity = studentMapper.toJpaEntity(student);
        StudentJpaEntity saved = springDataStudentRepository.save(jpaEntity);
        return studentMapper.toDomain(saved);
    }

    @Override
    public List<Student> findByCourseId(Long courseId) {
        return springDataStudentRepository.findByCourseIdOrderByLastNameAscFirstNameAsc(courseId)
                .stream()
                .map(studentMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Student> findByIdAndCourseId(Long id, Long courseId) {
        return springDataStudentRepository.findByIdAndCourseId(id, courseId)
                .map(studentMapper::toDomain);
    }

    @Override
    public Optional<Student> findById(Long id) {
        return springDataStudentRepository.findById(id)
                .map(studentMapper::toDomain);
    }

    @Override
    public boolean existsByCodeAndCourseId(String code, Long courseId) {
        return springDataStudentRepository.existsByCodeAndCourseId(code, courseId);
    }

    @Override
    public boolean existsByCodeAndCourseIdAndIdNot(String code, Long courseId, Long id) {
        return springDataStudentRepository.existsByCodeAndCourseIdAndIdNot(code, courseId, id);
    }

    @Override
    public void deleteById(Long id) {
        springDataStudentRepository.deleteById(id);
    }
}
