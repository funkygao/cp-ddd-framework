package io.github.dddplus.ast.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CallGraphEntryTest {

    @Test
    void callerNode() {
        CallGraphEntry entry = new CallGraphEntry("Caller", "m1", "Callee", "m2");
        assertEquals("Callee:m2", entry.calleeNode());
        assertEquals("Caller:m1", entry.callerNode());

        entry = new CallGraphEntry(
                "io.github.dddplus.Caller", "m1",
                "io.github.dddplus.Callee", "m2");
        assertEquals("io_github_dddplus_Callee:m2", entry.calleeNode());
        assertEquals("io_github_dddplus_Caller:m1", entry.callerNode());
    }

}