package io.github.dddplus.ast.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AggregateEntryTest {

    @Test
    void overlap() {
        AggregateEntry e1 = new AggregateEntry();
        e1.setPackageName("io.github.dddplus.order");
        AggregateEntry e2 = new AggregateEntry();
        e2.setPackageName("io.github.dddplus.task");
        assertFalse(e1.overlapWith(e2));
        assertFalse(e2.overlapWith(e1));
        e1.setPackageName("io.github.dddplus.task.domain");
        assertTrue(e1.overlapWith(e2));
        assertTrue(e2.overlapWith(e1));
    }

}