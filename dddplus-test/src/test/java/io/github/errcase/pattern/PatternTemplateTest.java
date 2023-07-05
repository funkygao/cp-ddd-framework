package io.github.errcase.pattern;

import io.github.dddplus.annotation.Pattern;
import io.github.dddplus.ext.IPatternFilter;
import io.github.dddplus.model.IBag;
import io.github.dddplus.runtime.BasePattern;
import io.github.dddplus.runtime.registry.mock.pattern.Patterns;
import lombok.NonNull;

public class PatternTemplateTest {

    @Pattern(code = Patterns.B2B)
    class B2BBasePattern extends BasePattern {

        private boolean match(Order order) {
            return order.getUpstream().equals("google");

        }

        private boolean match(FooTask fooTask) {
            return fooTask.getTaskType().contains("-2x-");

        }
    }

    @Pattern(code = Patterns.B2C)
    class B2CBasePattern extends BasePattern implements IPatternFilter {
        private boolean match(FooTask fooTask) {
            return fooTask.getTaskType().equals("2B");
        }

        @Override
        public IBag filter(@NonNull IBag bag) {
            return bag;
        }
    }
}
