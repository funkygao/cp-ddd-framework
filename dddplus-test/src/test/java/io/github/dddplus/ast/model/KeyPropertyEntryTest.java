package io.github.dddplus.ast.model;

import io.github.dddplus.dsl.KeyElement;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KeyPropertyEntryTest {

    @Test
    void displayName() {
        KeyPropertyEntry entry = new KeyPropertyEntry();
        entry.setName("name");
        assertEquals("name", entry.displayName(KeyElement.Type.Structural));
        entry.setRemark("remark");
        assertEquals("name/remark", entry.displayName(KeyElement.Type.Structural));
        entry.setJavadoc("foo");
        assertEquals("name/remark", entry.displayName(KeyElement.Type.Structural));
        assertEquals("<color:Red>name</color>/remark", entry.displayName(KeyElement.Type.Referential));
    }

}