package io.hhplus.course_registration.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"member_id", "course_info_id"})})
public class RegisterCourseHistory extends EntityTimestamp {

    public RegisterCourseHistory(Member member, CourseInfo courseInfo) {
        this.member = member;
        this.courseInfo = courseInfo;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long registerCourseHistoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_info_id")
    private CourseInfo courseInfo;

}
