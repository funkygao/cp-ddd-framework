package io.github.dddplus.ast.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KeyRuleEntryTest {

    @Test
    void actor() {
        KeyRuleEntry entry = new KeyRuleEntry();
        entry.setClassName("C");
        assertEquals(entry.actor(), "C");

        entry.setActor("D");
        assertEquals(entry.actor(), "D");
    }

    @Test
    void displayNameWithRemark() {
        KeyRuleEntry entry = new KeyRuleEntry();
        entry.setMethodName("M");
        assertEquals(entry.displayNameWithRemark(), "M");
        entry.setRemark("");
        assertEquals(entry.displayNameWithRemark(), "M");
        entry.setRemark("X");
        assertEquals(entry.displayNameWithRemark(), "M/X");
    }
}
