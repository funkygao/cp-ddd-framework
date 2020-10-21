/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.runtime;

/**
 * 调用扩展点超时抛出的异常.
 */
public class ExtTimeoutException extends RuntimeException {
    private final int timeoutInMs;

    ExtTimeoutException(int timeoutInMs) {
        this.timeoutInMs = timeoutInMs;
    }

    @Override
    public String getMessage() {
        return "timeout:" + timeoutInMs + "ms";
    }
}
