package io.hhplus.course_registration.repository;

import io.hhplus.course_registration.entity.CourseEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseEnrollmentRepository extends JpaRepository<CourseEnrollment, Long> {
    List<CourseEnrollment> findAllByCourseInfoCourseInfoIdIn(List<Long> courseInfoIdList);
}
