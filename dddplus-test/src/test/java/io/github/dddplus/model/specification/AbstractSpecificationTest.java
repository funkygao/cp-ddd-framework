package io.github.dddplus.model.specification;

import io.github.dddplus.model.spcification.AbstractSpecification;
import io.github.dddplus.model.spcification.ISpecification;
import io.github.dddplus.model.spcification.Notification;
import org.junit.Test;

import static org.junit.Assert.*;

public class AbstractSpecificationTest {

    class CheckTask {
        public int status;
        public int totalToCheckQty;

    }

    static class UuidSpec extends AbstractSpecification<CheckTask> {
        private String uuid;

        public UuidSpec(String uuid) {
            this.uuid = uuid;
        }

        @Override
        public boolean isSatisfiedBy(CheckTask candidate, Notification notification) {
            boolean ok = "11".equals(uuid);
            if (!ok) {
                notification.addError("UUID wrong");
            }
            return ok;
        }
    }

    static class NotFinishedSpec extends AbstractSpecification<CheckTask> {
        @Override
        public boolean isSatisfiedBy(CheckTask candidate, Notification notification) {
            return candidate.status != 3;
        }
    }

    static class CheckedQtyNotOverflowSpec extends AbstractSpecification<CheckTask> {
        private int toCheckQty;

        public CheckedQtyNotOverflowSpec(int toCheckQty) {
            this.toCheckQty = toCheckQty;
        }

        @Override
        public boolean isSatisfiedBy(CheckTask candidate, Notification notification) {
            boolean ok = candidate.totalToCheckQty >= toCheckQty;
            if (!ok) {
                notification.addError("qty exceeds expected");
            }
            return ok;
        }
    }

    @Test
    public void manualCheckScenario() {
        ISpecification<CheckTask> specsOfManualCheck = new NotFinishedSpec()
                .and(new CheckedQtyNotOverflowSpec(5))
                .and(new UuidSpec("11")); // 11 is from DTO
        CheckTask task = new CheckTask();
        task.totalToCheckQty = 100;
        assertTrue(specsOfManualCheck.isSatisfiedBy(task));
        Notification notification = Notification.build();
        assertTrue(specsOfManualCheck.isSatisfiedBy(task, notification));
        assertNull(notification.first());

        task.totalToCheckQty = 2;
        assertFalse(specsOfManualCheck.isSatisfiedBy(task, notification));
        assertEquals("qty exceeds expected", notification.first());
    }

    @Test
    public void or() {
        ISpecification<CheckTask> spec = new NotFinishedSpec()
                .or(new CheckedQtyNotOverflowSpec(10)
                        .and(new UuidSpec("12")));
        CheckTask task = new CheckTask();
        task.status = 2;
        task.totalToCheckQty = 123;
        assertTrue(spec.isSatisfiedBy(task));

        task.status = 3;
        assertFalse(spec.isSatisfiedBy(task));
    }

    @Test
    public void autoCheckScenario() {

    }


}