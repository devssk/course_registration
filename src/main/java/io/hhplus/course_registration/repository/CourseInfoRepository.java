package io.hhplus.course_registration.repository;

import io.hhplus.course_registration.entity.CourseInfo;
import io.hhplus.course_registration.entity.enums.CourseStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseInfoRepository extends JpaRepository<CourseInfo, Long> {
    List<CourseInfo> findByCourseStatus(CourseStatus courseStatus);
}
