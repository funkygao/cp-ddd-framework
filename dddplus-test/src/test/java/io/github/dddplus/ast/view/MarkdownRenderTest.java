package io.github.dddplus.ast.view;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class MarkdownRenderTest {

    @Test
    void x() throws IOException {
        MarkdownRender render = MarkdownRender.builder().build();
        render.render(null, null);
    }

}