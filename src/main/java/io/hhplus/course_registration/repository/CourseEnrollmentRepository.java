package io.hhplus.course_registration.repository;

import io.hhplus.course_registration.entity.CourseEnrollment;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;

public interface CourseEnrollmentRepository extends JpaRepository<CourseEnrollment, Long> {
    List<CourseEnrollment> findAllByCourseInfoCourseInfoIdIn(List<Long> courseInfoIdList);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    CourseEnrollment findByCourseInfoCourseInfoId(Long courseInfoId);
}
