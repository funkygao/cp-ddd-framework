package io.github.dddplus.ast.model;

import com.github.javaparser.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KeyFlowEntryTest {

    @Test
    void umlDisplayActualClass() {
        KeyFlowEntry entry = new KeyFlowEntry("Foo", "doSth", null);
        entry.setActor("Foo");
        assertEquals(entry.umlDisplayActualClass(), "");
        entry.setAbsolutePath("/root/xxx/A.java");
        Position position = new Position(12, 5);
        entry.setPosition(position);
        entry.setActor("bar");
        assertEquals(entry.umlDisplayActualClass(), "[[http://localhost:63342/api/file//root/xxx/A.java:12 Foo]]");
    }

}