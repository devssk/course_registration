package io.hhplus.course_registration.entity;

import io.hhplus.course_registration.entity.enums.CourseStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CourseInfo extends EntityTimestamp {

    public CourseInfo(Course course, Teacher teacher, Integer capacity, LocalDate courseDate, CourseStatus courseStatus) {
        this.course = course;
        this.teacher = teacher;
        this.capacity = capacity;
        this.courseDate = courseDate;
        this.courseStatus = courseStatus;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseInfoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @Column(nullable = false, columnDefinition = "TINYINT")
    private Integer capacity;

    @Column(nullable = false, columnDefinition = "DATE")
    private LocalDate courseDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "VARCHAR(10)")
    private CourseStatus courseStatus;

}
