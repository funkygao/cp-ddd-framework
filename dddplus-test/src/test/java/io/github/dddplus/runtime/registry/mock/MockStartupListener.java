package io.github.dddplus.runtime.registry.mock;

import io.github.dddplus.runtime.IStartupListener;
import org.springframework.stereotype.Component;

@Component
public class MockStartupListener implements IStartupListener {
    private static int called = 0;

    @Override
    public void onStartComplete() {
        called++;
    }

    public boolean isCalled() {
        return called > 0;
    }

    public static void reset() {
        called = 0;
    }

    public static int getCalled() {
        return called;
    }

}
