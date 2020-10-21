/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.runtime;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 命名的线程工厂.
 */
class NamedThreadFactory implements ThreadFactory {
    static final AtomicInteger poolCount = new AtomicInteger(0);
    private final AtomicInteger threadCount = new AtomicInteger(1);

    private final ThreadGroup group;
    private final String namePrefix;
    private final boolean daemon;

    NamedThreadFactory(String prefix, boolean daemon) {
        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
        namePrefix = prefix + "-" + poolCount.getAndIncrement() + "-T-";
        this.daemon = daemon;
    }

    @Override
    public Thread newThread(Runnable r) {
        // {prefix}-{poolCount}-T-{threadCount}
        Thread t = new Thread(group, r, namePrefix + threadCount.getAndIncrement(), 0);
        t.setDaemon(daemon);
        return t;
    }
}
