package org.example.cp.oms;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import static org.junit.Assert.fail;

class LogAssert {
    private static final String logFile = "logs/app.log";

    private static Scanner scanner = null;

    static void assertContains(String... events) throws FileNotFoundException {
        if (scanner == null) {
            scanner = new Scanner(new FileInputStream(logFile));
        }

        Set<String> expectedEvents = new HashSet<>(events.length);
        for (String event : events) {
            expectedEvents.add(event);
        }
        Set<String> foundEvents = new HashSet<>(events.length);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            for (String event : events) {
                if (line.contains(event)) {
                    foundEvents.add(event);
                }
            }
        }

        expectedEvents.removeAll(foundEvents);
        if (!expectedEvents.isEmpty()) {
            fail(String.format("%d events not found: %s", expectedEvents.size(), expectedEvents.toString()));
        }
    }
}
