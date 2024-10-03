package io.hhplus.course_registration.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Member extends EntityTimestamp {

    public Member(String memberName) {
        this.memberName = memberName;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false, columnDefinition = "VARCHAR(10)")
    private String memberName;

}
