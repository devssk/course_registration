package io.hhplus.course_registration.util;

import org.springframework.stereotype.Component;

@Component
public class Validation {

    public void validId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("아이디 값을 입력해주세요.");
        }
        if (id <= 0) {
            throw new IllegalArgumentException("아이디 값은 1 이상 이어야 합니다.");
        }
    }

}
