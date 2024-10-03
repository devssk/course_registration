package io.hhplus.course_registration.repository;

import io.hhplus.course_registration.entity.RegisterCourseHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegisterCourseHistoryRepository extends JpaRepository<RegisterCourseHistory, Long> {
    List<RegisterCourseHistory> findAllByMemberMemberId(Long memberId);
    List<RegisterCourseHistory> findAllByCourseInfoCourseInfoId(Long courseInfoId);
}
