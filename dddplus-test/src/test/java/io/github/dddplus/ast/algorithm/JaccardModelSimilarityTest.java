package io.github.dddplus.ast.algorithm;

import io.github.dddplus.ast.algorithm.IKeyModelSimilarity;
import io.github.dddplus.ast.model.KeyModelEntry;
import io.github.dddplus.ast.model.KeyPropertyEntry;
import io.github.dddplus.dsl.KeyElement;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class JaccardModelSimilarityTest {

    @Test
    void demo() {
        KeyModelEntry m1 = new KeyModelEntry("c1");
        KeyModelEntry m2 = new KeyModelEntry("c2");
        IKeyModelSimilarity modelSimilarity = new JaccardModelSimilarity();
        double similarity = modelSimilarity.similarity(m1, m2);
        assertTrue(similarity < 0.01);
        KeyPropertyEntry p11 = new KeyPropertyEntry();
        p11.setName("a");
        KeyPropertyEntry p12 = new KeyPropertyEntry();
        p12.setName("b");
        m1.addField(KeyElement.Type.Structural, p11);
        m1.addField(KeyElement.Type.Structural, p12);
        KeyPropertyEntry p21 = new KeyPropertyEntry();
        p21.setName("a");
        m2.addField(KeyElement.Type.Structural, p21);
        similarity = modelSimilarity.similarity(m1, m2);
        assertTrue(similarity > 29); // 30%
    }

}