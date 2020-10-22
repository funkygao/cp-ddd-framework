package io.github.dddplus.runtime;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Slf4j
public class NamedThreadFactoryTest {

    @Before
    public void setUp() {
        NamedThreadFactory.poolCount.set(0);
    }

    @Test
    public void threadName() throws InterruptedException {
        final int N = 5;
        final String prefix = "foo";
        Set<String> threadNames = new ConcurrentSkipListSet<>();
        final CountDownLatch waitGroup = new CountDownLatch(N);
        ThreadFactory threadFactory = new NamedThreadFactory(prefix, false);
        for (int i = 0; i < N; i++) {
            Thread thread = threadFactory.newThread(() -> {
                threadNames.add(Thread.currentThread().getName());
                waitGroup.countDown();
            });
            thread.start();
        }

        waitGroup.await();

        // another thread pool with the same prefix
        Set<String> threadNames1 = new ConcurrentSkipListSet<>();
        final CountDownLatch waitGroup1 = new CountDownLatch(N);
        threadFactory = new NamedThreadFactory(prefix, false);
        for (int i = 0; i < N; i++) {
            Thread thread = threadFactory.newThread(() -> {
                threadNames1.add(Thread.currentThread().getName());
                waitGroup1.countDown();
            });
            thread.start();
        }

        waitGroup1.await();

        // pool0
        assertEquals(N, threadNames.size());
        for (int i = 0; i < N; i++) {
            // thread name: {prefix}-{poolCount}-T-{threadCount}
            // e,g. foo-0-T-2
            log.info("{} {} {}", i + 1, threadNames, threadNames1);
            assertTrue(threadNames.contains("foo-0-T-" + (i + 1)));
        }

        // pool1
        assertEquals(N, threadNames1.size());
        for (int i = 0; i < N; i++) {
            log.info("{} {} {}", i + 1, threadNames, threadNames1);
            assertTrue(threadNames1.contains("foo-1-T-" + (i + 1)));
        }
    }

}