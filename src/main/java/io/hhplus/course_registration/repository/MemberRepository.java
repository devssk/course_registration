package io.hhplus.course_registration.repository;

import io.hhplus.course_registration.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
