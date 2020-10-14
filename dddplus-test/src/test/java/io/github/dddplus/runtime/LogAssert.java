package io.github.dddplus.runtime;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.fail;

// a unit test assert tool: assert sth exists in log file.
public class LogAssert {
    private static final String logFile = "logs/app.log";
    private static RandomAccessFile reader = null;

    public static void assertContains(String... events) throws IOException {
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
            fail(String.format("%d events not found: %s", expectedEvents.size(), expectedEvents.toString()));
        }
    }
}
