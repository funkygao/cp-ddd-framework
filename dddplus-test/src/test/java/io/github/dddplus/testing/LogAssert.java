/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.testing;

import org.junit.Assert;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashSet;
import java.util.Set;

/**
 * A unit test assert tool: assert sth exists in log file: logs/app.log.
 * <p>
 * <p>IMPORTANT: LogAssert is stateful! Each assert will reach log EOF.</p>
 * <p>You cannot assert twice on the same log events state.</p>
 * <p>你需要配置{@code log4j2.xml}，以便把测试的日志输出到{@code logs/app.log}</p>
 */
public class LogAssert {
    private static final String logFile = "logs/app.log";
    private static RandomAccessFile reader = null;

    private LogAssert() {
    }

    /**
     * Assert that the log file not contains specified events(log message).
     *
     * @param events log message, a single log file line
     * @throws IOException
     */
    public static synchronized void assertNotContains(String... events) throws IOException {
        try {
            assertContains(events);
            Assert.fail();
        } catch (AssertionError ignored) {
            // 就该出现该异常
        }
    }

    /**
     * Assert that the log file contains specified events(log message).
     *
     * @param events log message, a single log file line
     * @throws IOException
     */
    public static synchronized void assertContains(String... events) throws IOException {
        Set<String> expectedEvents = new HashSet<>(events.length);
        for (String event : events) {
            expectedEvents.add(event);
        }
        Set<String> foundEvents = new HashSet<>(events.length);

        if (reader == null) {
            reader = new RandomAccessFile(logFile, "r");
        }
        while (true) {
            String line = reader.readLine();
            if (line == null) {
                // EOF
                break;
            }

            line = new String(line.getBytes("ISO-8859-1"), "utf-8");
            for (String event : events) {
                if (line.contains(event)) {
                    foundEvents.add(event);
                }
            }
        }

        // 差集
        expectedEvents.removeAll(foundEvents);
        if (!expectedEvents.isEmpty()) {
            Assert.fail(String.format("%d events not found: %s", expectedEvents.size(), expectedEvents.toString()));
        }
    }
}
