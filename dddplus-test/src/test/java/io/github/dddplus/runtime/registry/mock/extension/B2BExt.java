package io.github.dddplus.runtime.registry.mock.extension;

import io.github.dddplus.annotation.Extension;
import io.github.dddplus.annotation.Governance;
import io.github.dddplus.runtime.registry.mock.ext.IFooExt;
import io.github.dddplus.runtime.registry.mock.model.FooModel;
import io.github.dddplus.runtime.registry.mock.pattern.B2BPattern;

@Extension(code = B2BPattern.CODE)
public class B2BExt implements IFooExt {
    @Override
    @Governance
    public Integer execute(FooModel model) {
        if (model.isWillThrowRuntimeException()) {
            throw new RuntimeException("runtime ex on purpuse");
        }

        if (model.isWillThrowOOM()) {
            throw new OutOfMemoryError("OOM on purpose");
        }

        if (model.isWillSleepLong()) {
            try {
                Thread.sleep(2 << 10); // 2s
            } catch (InterruptedException e) {
            }
        }

        throw new RuntimeException("b2b error on purpose");
    }
}
