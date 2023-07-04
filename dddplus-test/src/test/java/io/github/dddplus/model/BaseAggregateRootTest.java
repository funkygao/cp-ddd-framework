package io.github.dddplus.model;

import io.github.dddplus.model.spcification.Notification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BaseAggregateRootTest {

    @Test
    void basic() {
        Student student = Student.builder().id(5L).build();
        student.dirty(new NameChanged(student));
        NameChanged hint = student.firstHintOf(NameChanged.class);
        assertEquals(hint.student.id, 5L);
        student.exchangeSet("key", "value");
        try {
            student.exchangeGet("key", Integer.class);
            fail();
        } catch (ClassCastException expected) {
        }
        assertEquals(student.exchangeGet("key", String.class), "value");
        student.exchangeSet("k1", true);
        assertTrue(student.exchangeIs("k1"));
        assertFalse(student.exchangeIs("non-exist"));
        try {
            student.exchangeIs("key");
            fail();
        } catch (ClassCastException expected) {
        }

        assertNull(student.exchangeGet("blah", Float.class));
    }

    @Builder
    static class Student extends BaseAggregateRoot<Student> {
        Long id;

        @Override
        protected void onNotSatisfied(Notification notification) {

        }
    }

    @AllArgsConstructor
    static class NameChanged implements IDirtyHint {
        private final Student student;
    }


}
