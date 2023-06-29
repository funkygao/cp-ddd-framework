/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast.parser;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

/**
 * Java AST Node 工具.
 */
public final class JavaParserUtil {
    private static final String BLANK = "";

    private JavaParserUtil() {
    }

    public static String packageName(ClassOrInterfaceDeclaration classOrInterfaceDeclaration) {
        Node node = classOrInterfaceDeclaration.getParentNode().get();
        for (; ; ) {
            if (node instanceof CompilationUnit) {
                CompilationUnit cu = (CompilationUnit) node;
                return cu.getPackageDeclaration().get().getNameAsString();
            }

            node = node.getParentNode().get();
        }
    }

    /**
     * 获取指定节点的类或接口声明.
     *
     * @param node AST Node
     * @return 如果指定的节点不是类也不是接口，返回null
     */
    public static ClassOrInterfaceDeclaration getClass(Node node) {
        while (!(node instanceof ClassOrInterfaceDeclaration)) {
            if (node.getParentNode().isPresent()) {
                node = node.getParentNode().get();
            } else {
                // node is not class nor interface
                return null;
            }
        }

        return (ClassOrInterfaceDeclaration) node;
    }

    public static boolean implementsInterface(ClassOrInterfaceDeclaration classOrInterfaceDeclaration, Class theInterface) {
        for (ClassOrInterfaceType t : classOrInterfaceDeclaration.getImplementedTypes()) {
            if (t.getNameAsString().equals(theInterface.getSimpleName())) {
                return true;
            }
        }

        return false;
    }

    static String extractJavadocContent(Comment comment) {
        if (comment instanceof LineComment) {
            return comment.getContent().split("\n")[0].replace("/", "").trim();
        }

        final String commentContent = comment.getContent();
        String[] lines = commentContent.split("\n");
        int idx = 1;
        if (lines.length == 1) {
            idx = 0;
        }
        String info = lines[idx].replace("*", "").trim();
        if (info.startsWith("@") || info.startsWith("{@")) {
            return BLANK;
        }
        return info;
    }

    public static String javadocFirstLineOf(FieldDeclaration fieldDeclaration) {
        if (!fieldDeclaration.getComment().isPresent()) {
            return BLANK;
        }

        return extractJavadocContent(fieldDeclaration.getComment().get());
    }

    public static String javadocFirstLineOf(MethodDeclaration methodDeclaration) {
        if (!methodDeclaration.getComment().isPresent()) {
            return BLANK;
        }

        return extractJavadocContent(methodDeclaration.getComment().get());
    }

    public static String javadocFirstLineOf(ClassOrInterfaceDeclaration classOrInterfaceDeclaration) {
        if (!classOrInterfaceDeclaration.getComment().isPresent()) {
            return BLANK;
        }

        return extractJavadocContent(classOrInterfaceDeclaration.getComment().get());
    }
}
