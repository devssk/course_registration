package io.hhplus.course_registration.repository;

import io.hhplus.course_registration.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
