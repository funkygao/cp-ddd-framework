package io.github.dddplus.ast.parser;

import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.comments.LineComment;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JavaParserUtilTest {

    @Test
    void javadocFirstLine() {
        Comment comment = new LineComment();
        comment.setContent("// abc df   ");
        assertEquals(JavaParserUtil.extractJavadocContent(comment), "abc df");
        String content = "// abc df\n" +
                "// bd";
        assertEquals(JavaParserUtil.extractJavadocContent(comment), "abc df");

        comment = new JavadocComment();
        content = "/**\n" +
                " * Java AST Node 工具.\n" +
                " * adfadfa\n" +
                " */";
        comment.setContent(content);
        assertEquals(JavaParserUtil.extractJavadocContent(comment), "Java AST Node 工具.");

        comment.setContent(" 单号");
        assertEquals(JavaParserUtil.extractJavadocContent(comment), "单号");

        comment.setContent("/**\n" +
                " * 只加载一个{@link Container}的{@link Task}.\n" +
                " */");
        assertEquals(JavaParserUtil.extractJavadocContent(comment), "只加载一个Container的Task.");
    }

    @Test
    void resolvedTypeAsString() {
        assertEquals(JavaParserUtil.resolvedTypeAsString("ddd.plus.showcase.wms.domain.order.OrderBag"), "OrderBag");
        assertEquals(JavaParserUtil.resolvedTypeAsString("java.util.List<ddd.plus.showcase.wms.domain.common.Platform>"), "List");
        assertEquals(JavaParserUtil.resolvedTypeAsString("Ext"), "Ext");
        assertEquals(JavaParserUtil.resolvedTypeAsString("java.util.Set<ddd.plus.showcase.wms.domain.order.OrderNo>"), "Set");
        assertEquals(JavaParserUtil.resolvedTypeAsString("? super ddd.plus.showcase.wms.domain.carton.Carton"), "Carton");
    }

}