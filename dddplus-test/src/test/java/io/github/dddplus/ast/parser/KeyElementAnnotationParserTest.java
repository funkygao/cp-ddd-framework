package io.github.dddplus.ast.parser;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class KeyElementAnnotationParserTest {

    @Test
    public void keyRelationTypeOf() {
        KeyElementAnnotationParser parser = new KeyElementAnnotationParser(null, null, "");
        KeyElementAnnotationParser.RelationToClazz relation = parser.keyRelationTypeOf("String");
        assertNull(relation);
        relation = parser.keyRelationTypeOf("List");
        assertNull(relation);
        relation = parser.keyRelationTypeOf("List<string>");
        assertNull(relation);
        relation = parser.keyRelationTypeOf("contextual<string>");
        assertNull(relation);
        relation = parser.keyRelationTypeOf("HasMany<ShipmentOrder>");
        assertEquals(relation.getRelationType(), "HasMany");
        assertEquals(relation.getRightClass(), "ShipmentOrder");
    }

}