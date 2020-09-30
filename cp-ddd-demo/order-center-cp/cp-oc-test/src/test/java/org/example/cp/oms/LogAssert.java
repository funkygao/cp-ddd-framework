package org.example.cp.oms;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import static org.junit.Assert.assertTrue;

class LogAssert {
    private static final String logFile = "logs/app.log";

    static void assertContains(String... events) throws FileNotFoundException {
        Scanner scanner = new Scanner(new FileInputStream(logFile));
        Set<String> foundEvents = new HashSet<>(events.length);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            for (String event : events) {
                if (line.contains(event)) {
                    foundEvents.add(event);
                }
            }
        }

        assertTrue(foundEvents.size() == events.length);
    }

    static void cleanUp() {
        new File(logFile).delete();
    }
}
