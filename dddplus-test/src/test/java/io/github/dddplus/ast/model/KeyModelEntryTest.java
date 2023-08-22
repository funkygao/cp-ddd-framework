package io.github.dddplus.ast.model;

import io.github.dddplus.dsl.KeyElement;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KeyModelEntryTest {

    @Test
    void displayFieldByType() {
        KeyModelEntry entry = new KeyModelEntry("Foo");
        for (int i = 0; i < 8; i++) {
            KeyPropertyEntry propertyEntry = new KeyPropertyEntry();
            propertyEntry.setName(String.valueOf(i));
            entry.addField(KeyElement.Type.Structural, propertyEntry);
        }

        assertEquals(entry.displayFieldByType(KeyElement.Type.Structural), "0 1 2 3 4 \n" +
                " 5 6 7");
    }

}