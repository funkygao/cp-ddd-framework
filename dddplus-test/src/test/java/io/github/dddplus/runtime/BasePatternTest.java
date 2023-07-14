package io.github.dddplus.runtime;

import io.github.dddplus.annotation.Pattern;
import io.github.dddplus.ext.IIdentity;
import io.github.dddplus.runtime.pattern.ExCarton;
import io.github.dddplus.runtime.pattern.ExOrder;
import io.github.dddplus.runtime.pattern.ExTask;
import io.github.dddplus.runtime.pattern.PledgePattern;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.*;

@Slf4j
public class BasePatternTest {

    @Test
    public void basic() {
        BasePattern pattern = new PresalePattern();
        CheckTask task = new CheckTask();
        task.setPresaleFlag(true);
        assertTrue(pattern.match(task));
        assertTrue(pattern.match(task));
        Order order = new Order();
        assertFalse(pattern.match(order));
        order.setMoneyCollected(5);
        assertTrue(pattern.match(order));
        order.setMoneyCollected(100);
        assertFalse(pattern.match(order));
        Carton carton = new Carton();
        assertTrue(pattern.match(carton));
    }

    @Test
    public void basicExternalClasses() {
        BasePattern pattern = new PledgePattern();
        ExTask task = new ExTask();
        task.setPresaleFlag(true);
        assertTrue(pattern.match(task));
        assertTrue(pattern.match(task));
        ExOrder order = new ExOrder();
        assertFalse(pattern.match(order));
        order.setMoneyCollected(5);
        assertTrue(pattern.match(order));
        order.setMoneyCollected(100);
        assertFalse(pattern.match(order));
        ExCarton carton = new ExCarton();
        assertTrue(pattern.match(carton));
    }

    @Test
    public void nullModel() {
        BasePattern pattern = new PresalePattern();
        try {
            pattern.match(null);
            fail();
        } catch (NullPointerException expected) {
            assertTrue(expected.getMessage().contains("is marked non-null but is null"));
        }
    }

    @Test
    public void inheritance() {
        BasePattern pattern = new SubPresalePattern();
        Order order = new Order();
        assertFalse(pattern.match(order));
        order.setMoneyCollected(5);
        assertTrue(pattern.match(order));
        order.setMoneyCollected(110);
        assertFalse(pattern.match(order));
        CheckTask task = new CheckTask();
        try {
            // 父类的match(CheckTask)是private，无法继承
            pattern.match(task);
            fail();
        } catch (IllegalArgumentException expected) {
            // io.github.dddplus.runtime.BasePatternTest$SubPresalePattern match method NoSuchMethodException io.github.dddplus.runtime.BasePatternTest$SubPresalePattern.match(io.github.dddplus.runtime.BasePatternTest$CheckTask)
            assertTrue(expected.getMessage().contains(" match method NoSuchMethodException"));
            assertTrue(expected.getMessage().contains(".match("));
        }
    }

    @Test
    public void matchMethodNotFound() {
        BasePattern pattern = new PresalePattern();
        MatchNotSupportedModel model = new MatchNotSupportedModel();
        try {
            // Pattern没有定义该model的match方法
            pattern.match(model);
            fail();
        } catch (IllegalArgumentException expected) {
            assertTrue(expected.getCause() instanceof NoSuchMethodException);
            assertTrue(expected.getMessage().contains(" match method NoSuchMethodException "));
        }
    }

    @Execution(ExecutionMode.CONCURRENT)
    @RepeatedTest(value = 20)
    void concurrentTesting(TestInfo testInfo) {
        log.info("concurrentTesting {}", testInfo.getDisplayName());

        BasePattern pattern = new SubPresalePattern();
        Order order = new Order();
        assertFalse(pattern.match(order));
        order.setMoneyCollected(5);
        assertTrue(pattern.match(order));
    }

    @Test
    public void matchMethodWronglyStatic() {
        BasePattern pattern = new SubPresalePattern();
        PickTask model = new PickTask();
        try {
            // Pattern没有定义该model的match方法
            // static方法无法继承
            pattern.match(model);
            fail();
        } catch (IllegalArgumentException expected) {
            assertTrue(expected.getCause() instanceof NoSuchMethodException);
            assertTrue(expected.getMessage().contains(" match method NoSuchMethodException "));
        }

        pattern = new PresalePattern();
        // 如果研发写错了，写成了static方法，DDDplus应该跑异常吗？
        // BasePattern是Spring管理的单例，里面也不该有状态，不抛异常了
        assertTrue(pattern.match(model));
    }

    @Test
    public void matchWillThrowException() {
        AlwaysFail alwaysFail = new AlwaysFail();
        BasePattern pattern = new SubPresalePattern();
        alwaysFail.code = 1;
        try {
            pattern.match(alwaysFail);
            fail();
        } catch (RuntimeException expected) {
            assertEquals("1", expected.getMessage());
        }

        alwaysFail.code = 2;
        try {
            pattern.match(alwaysFail);
            fail();
        } catch (Exception expected) {
            assertTrue(expected.getMessage().contains("See cause: 2.1"));
        }

        alwaysFail.code = 4;
        try {
            pattern.match(alwaysFail);
            fail();
        } catch (RuntimeException expected) {
            assertTrue(expected.getMessage().contains("See cause: 4"));
            assertTrue(expected.getMessage().contains("SubPresalePattern.match(AlwaysFail) failed"));
        }

        alwaysFail.code = 5;try {
            pattern.match(alwaysFail);
            fail();
        } catch (ArithmeticException expected) {
            assertEquals("/ by zero", expected.getMessage());
        }
    }

    @Pattern(code = "presale")
    static class PresalePattern extends BasePattern {
        private Boolean match(CheckTask task) {
            log.info("PresalePattern.-match(task)");
            return task.getPresaleFlag() != null && task.presaleFlag;
        }

        public boolean match(Order order) {
            log.info("PresalePattern.+match(order)");
            return order.getMoneyCollected() != null && order.getMoneyCollected() < 10;
        }

        protected boolean match(Carton carton) {
            log.info("PresalePattern.#match(carton)");
            return carton != null;
        }

        // 故意错：static
        protected static boolean match(PickTask pickTask) {
            return true;
        }
    }

    @Pattern(code = "xx")
    static class SubPresalePattern extends PresalePattern {
        private boolean match(AlwaysFail any) throws InvocationTargetException, IllegalAccessException {
            switch (any.code) {
                case 1:
                    throw new RuntimeException("1");
                case 2:
                    throw new InvocationTargetException(null,"2.1"); // 非RuntimeException
                case 4:
                    OutOfMemoryError error = new OutOfMemoryError();
                    throw new InvocationTargetException(error, "4");
                case 3:
                    throw new IllegalAccessException("3");
                case 5:
                    int b = 5 / 0;
                    return false;
                default:
                    return true;
            }
        }
    }


    @Data
    static class CheckTask implements IIdentity {
        Boolean presaleFlag;
    }

    @Data
    static class Order implements IIdentity {
        Integer moneyCollected;
    }

    @Data
    static class Carton implements IIdentity {
    }

    static class MatchNotSupportedModel implements IIdentity {
    }


    static class AlwaysFail implements IIdentity {
        int code;
    }

    static class PickTask implements IIdentity {
    }

}