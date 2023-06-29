package io.github.dddplus.ast.parser;

import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.comments.LineComment;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
    }

}