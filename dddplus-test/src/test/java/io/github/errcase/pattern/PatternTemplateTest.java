package io.github.errcase.pattern;

import io.github.dddplus.annotation.Pattern;
import io.github.dddplus.runtime.PatternTemplate;
import io.github.dddplus.runtime.registry.mock.pattern.Patterns;

public class PatternTemplateTest {

    @Pattern(code = Patterns.B2B)
    class B2BPatternTemplate extends PatternTemplate {

        private boolean match(Order order) {
            return order.getUpstream().equals("google");

        }

        private boolean match(Task task) {
            return task.getTaskType().contains("-2x-");

        }
    }

    @Pattern(code = Patterns.B2C)
    class B2CPatternTemplate extends PatternTemplate {
        private boolean match(Task task) {
            return task.getTaskType().equals("2B");
        }
    }
}
