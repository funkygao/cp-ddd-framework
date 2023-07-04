package io.github.dddplus.model.specification;

import io.github.dddplus.model.spcification.AbstractSpecification;
import io.github.dddplus.model.spcification.ISpecification;
import io.github.dddplus.model.spcification.Notification;
import org.junit.Test;

import static org.junit.Assert.*;

public class AndOrSpecificationTest {
    static class IntegerGreaterThanSpec extends AbstractSpecification<Integer> {
        private final Integer value;

        IntegerGreaterThanSpec(Integer value) {
            this.value = value;
        }

        @Override
        public boolean isSatisfiedBy(Integer candidate, Notification notification) {
            if (candidate <= value) {
                notification.addError(String.format("candidate:%d is not more than %d", candidate, value));
                return false;
            }

            return true;
        }
    }

    @Test
    public void chainOfAnd() {
        ISpecification<Integer> spec = new IntegerGreaterThanSpec(1)
                .and(new IntegerGreaterThanSpec(2))
                .and(new IntegerGreaterThanSpec(3));
        assertTrue(spec.isSatisfiedBy(4));

        for (int i = 1; i < 4; i++) {
            assertFalse(spec.isSatisfiedBy(3));
            Notification notification = Notification.build();
            assertFalse(spec.isSatisfiedBy(i, notification));
            assertEquals("candidate:" + i + " is not more than " + i, notification.first());
        }
    }

    @Test
    public void chainOfOr() {
        ISpecification<Integer> spec = new IntegerGreaterThanSpec(1)
                .or(new IntegerGreaterThanSpec(2))
                .or(new IntegerGreaterThanSpec(3));
        assertTrue(spec.isSatisfiedBy(4));
        assertTrue(spec.isSatisfiedBy(3));
        assertTrue(spec.isSatisfiedBy(2));
        assertFalse(spec.isSatisfiedBy(1));
        assertTrue(spec.isSatisfiedBy(100));
    }

}