package org.ddd.cp.ddd.runtime.registry.mock;

import org.ddd.cp.ddd.annotation.Extension;
import org.ddd.cp.ddd.runtime.registry.mock.ext.IFooExt;
import org.ddd.cp.ddd.runtime.registry.mock.model.FooModel;
import org.ddd.cp.ddd.runtime.registry.mock.pattern.B2BPattern;

@Extension(code = B2BPattern.CODE)
public class B2BExt implements IFooExt {
    @Override
    public Integer execute(FooModel model) {
        if (model.isWillThrowRuntimeException()) {
            throw new RuntimeException("runtime ex on purpuse");
        }

        if (model.isWillSleepLong()) {
            try {
                Thread.sleep(100 << 10); // 100s
            } catch (InterruptedException e) {
            }
        }

        throw new RuntimeException("b2b error");
    }
}
