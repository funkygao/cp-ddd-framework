package io.github.dddplus.ast.view;

import io.github.dddplus.ast.model.CallGraphEntry;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CallGraphRendererTest {

    @Test
    void topReferencedCallee() {
        CallGraphRenderer renderer = new CallGraphRenderer();
        CallGraphEntry entry = new CallGraphEntry(null, "A", "foo", "B", "bar");
        renderer.incrementCounter(entry);
        assertEquals(1, renderer.topReferencedCallee(5).size());
    }

}