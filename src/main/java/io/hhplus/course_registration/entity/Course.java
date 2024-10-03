package io.hhplus.course_registration.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Course extends EntityTimestamp {

    public Course(String courseName) {
        this.courseName = courseName;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId;

    @Column(nullable = false, columnDefinition = "VARCHAR(10)")
    private String courseName;


}
