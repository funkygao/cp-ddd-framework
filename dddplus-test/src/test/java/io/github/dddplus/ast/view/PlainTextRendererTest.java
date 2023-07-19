package io.github.dddplus.ast.view;

import io.github.dddplus.dsl.KeyElement;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlainTextRendererTest {

    @Test
    void keyElementTypeMaxLength() {
        int max = -1;
        for (KeyElement.Type type : KeyElement.Type.values()) {
            if (type.toString().length() > max) {
                max = type.toString().length();
            }
        }
        // 如果失败，说明 KeyElement.Type 变化了，需要修改 PlainTextBuilder#writeClazzDefinition
        assertTrue(max == 13);
    }

}