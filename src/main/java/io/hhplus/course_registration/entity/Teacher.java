package io.hhplus.course_registration.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Teacher extends EntityTimestamp {

    public Teacher(String teacherName) {
        this.teacherName = teacherName;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teacherId;

    @Column(nullable = false, columnDefinition = "VARCHAR(10)")
    private String teacherName;

}
