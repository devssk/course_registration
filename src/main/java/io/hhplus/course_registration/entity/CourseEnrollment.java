package io.hhplus.course_registration.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CourseEnrollment extends EntityTimestamp {

    public CourseEnrollment(CourseInfo courseInfo, Integer enrollment) {
        this.courseInfo = courseInfo;
        this.enrollment = enrollment;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseEnrollmentId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_info_id")
    private CourseInfo courseInfo;

    @Column(nullable = false, columnDefinition = "TINYINT")
    private Integer enrollment;

    public void plusEnrollment() {
        this.enrollment += 1;
    }

}
