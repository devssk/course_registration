package io.hhplus.course_registration;

import io.hhplus.course_registration.util.Validation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ValidationTest {

    Validation validation;

    @BeforeEach
    void setUp() {
        validation = new Validation();
    }

    @Test
    @DisplayName("ID값이 null일 경우")
    public void nullCheckIdTest() {
        // given
        Long id = null;

        // when
        Throwable throwable = assertThrows(IllegalArgumentException.class, () -> validation.validId(id));

        // then
        assertEquals("아이디 값을 입력해주세요.", throwable.getMessage());
    }

    @Test
    @DisplayName("ID값이 0 이하인 경우")
    public void rangeCheckIdTest() {
        // given
        Long id1 = -1L;
        Long id2 = 0L;

        // when
        Throwable throwable1 = assertThrows(IllegalArgumentException.class, () -> validation.validId(id1));
        Throwable throwable2 = assertThrows(IllegalArgumentException.class, () -> validation.validId(id2));

        // then
        assertAll(() -> {
            assertEquals("아이디 값은 1 이상 이어야 합니다.", throwable1.getMessage());
            assertEquals("아이디 값은 1 이상 이어야 합니다.", throwable2.getMessage());
        });
    }

}
