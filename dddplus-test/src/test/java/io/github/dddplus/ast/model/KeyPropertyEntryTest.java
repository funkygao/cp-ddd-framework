package io.github.dddplus.ast.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KeyPropertyEntryTest {

    @Test
    void displayName() {
        KeyPropertyEntry entry = new KeyPropertyEntry();
        entry.setName("name");
        assertEquals("name", entry.displayName());
        entry.setRemark("remark");
        assertEquals("name/remark", entry.displayName());
        entry.setJavadoc("foo");
        assertEquals("name/foo/remark", entry.displayName());
    }

}