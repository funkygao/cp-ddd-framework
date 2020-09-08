package org.x.cp.ddd.model;

/**
 * 调用扩展点超时抛出的异常.
 */
public class ExtTimeoutException extends RuntimeException {
    private final int timeoutInMs;

    public ExtTimeoutException(int timeoutInMs) {
        this.timeoutInMs = timeoutInMs;
    }

    @Override
    public String getMessage() {
        return "timeout:" + timeoutInMs + "ms";
    }
}
