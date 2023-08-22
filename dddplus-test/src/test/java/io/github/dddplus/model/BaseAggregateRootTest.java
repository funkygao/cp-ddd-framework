package io.github.dddplus.model;

import io.github.dddplus.model.IMergeAwareDirtyHint;
import io.github.dddplus.model.spcification.AbstractSpecification;
import io.github.dddplus.model.spcification.Notification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BaseAggregateRootTest {

    @Test
    void basic() {
        Student student = Student.builder().id(5L).build();
        student.dirty(new NameChanged(student));
        NameChanged hint = student.firstHintOf(NameChanged.class);
        assertEquals(hint.student.id, 5L);
        student.xSet("key", "value");
        try {
            student.xGet("key", Integer.class);
            fail();
        } catch (ClassCastException expected) {
        }
        assertEquals(student.xGet("key", String.class), "value");
        student.xSet("k1", true);
        assertTrue(student.xIs("k1"));
        assertFalse(student.xIs("non-exist"));
        try {
            student.xIs("key");
            fail();
        } catch (ClassCastException expected) {
        }

        assertNull(student.xGet("blah", Float.class));
    }

    @Test
    void specification() {
        Student student = Student.builder().id(5L).build();
        try {
            student.assureSatisfied(new IdGreaterThanTen());
            fail();
        } catch (RuntimeException expected) {
            assertEquals("bad 5", expected.getMessage());
        }

        student = Student.builder().id(56L).build();
        student.assureSatisfied(new IdGreaterThanTen());
    }

    @Test
    void mergeDirtyWith() {
        Student student = Student.builder().id(5L).build();
        student.mergeDirtyWith(new StudentPassExamHint(student));
        student.mergeDirtyWith(new StudentPassExamHint(student));
        assertEquals(student.firstHintOf(StudentPassExamHint.class).getId(), 5);
    }

    @Builder
    static class Student extends BaseAggregateRoot<Student> {
        Long id;

        @Override
        protected void whenNotSatisfied(Notification notification) {
            throw new RuntimeException(String.format("%s %d", notification.first(), notification.firstArgs()[0]));
        }
    }

    static class IdGreaterThanTen extends AbstractSpecification<Student> {
        @Override
        public boolean isSatisfiedBy(Student student, Notification notification) {
            if (student.id <= 10) {
                notification.addError("bad", student.id);
                return false;
            }
            return true;
        }
    }

    @Getter
    static class StudentPassExamHint implements IMergeAwareDirtyHint<Long> {
        private final Student student;

        public StudentPassExamHint(Student student) {
            this.student = student;
        }

        @Override
        public Long getId() {
            return student.id;
        }

    }

    @AllArgsConstructor
    @Getter
    static class NameChanged implements IDirtyHint {
        private final Student student;
    }


}
