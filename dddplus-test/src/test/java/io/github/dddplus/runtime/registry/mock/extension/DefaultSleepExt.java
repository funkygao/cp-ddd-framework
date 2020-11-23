package io.github.dddplus.runtime.registry.mock.extension;

import io.github.dddplus.annotation.Extension;
import io.github.dddplus.runtime.registry.mock.ext.ISleepExt;

@Extension(code = DefaultSleepExt.DefaultCode)
public class DefaultSleepExt implements ISleepExt {

    @Override
    public void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException ignored) {
        }
    }
}
