package io.hhplus.course_registration.repository;

import io.hhplus.course_registration.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
}
