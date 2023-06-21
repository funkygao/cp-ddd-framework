package io.github.dddplus.ast.model;

import io.github.dddplus.dsl.KeyElement;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KeyModelEntryTest {

    @Test
    void displayUndefinedTypes() {
        KeyModelEntry entry = new KeyModelEntry("CheckTask");
        KeyPropertyEntry propertyEntry = new KeyPropertyEntry();
        propertyEntry.setName("xx");
        entry.addField(KeyElement.Type.Structural, propertyEntry);
        assertEquals("Referential Lifecycle Operational", entry.displayUndefinedTypes());
    }

}