package org.cdf.ddd.runtime.registry.mock.extension;

import org.cdf.ddd.annotation.Extension;
import org.cdf.ddd.runtime.registry.mock.ext.IFooExt;
import org.cdf.ddd.runtime.registry.mock.model.FooModel;
import org.cdf.ddd.runtime.registry.mock.pattern.B2BPattern;

@Extension(code = B2BPattern.CODE)
public class B2BExt implements IFooExt {
    @Override
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
