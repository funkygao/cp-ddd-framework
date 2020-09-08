package org.x.cp.ddd.runtime.registry;

/**
 * DDD框启动过程中进行注册阶段产生的异常.
 */
class BootstrapException extends RuntimeException {

    private BootstrapException(String message) {
        super(message);
    }

    public static BootstrapException ofMessage(String... message) {
        StringBuilder sb = new StringBuilder(100);
        for (String s : message) {
            sb.append(s);
        }

        return new BootstrapException(sb.toString());
    }
}
