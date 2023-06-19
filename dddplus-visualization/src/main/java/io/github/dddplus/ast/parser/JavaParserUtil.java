/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast.parser;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

/**
 * Java AST Node 工具.
 */
public final class JavaParserUtil {
    private static final String BLANK = "";

    private JavaParserUtil() {
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

    public static String javadocFirstLineOf(FieldDeclaration fieldDeclaration) {
        if (!fieldDeclaration.getComment().isPresent()) {
            return BLANK;
        }

        final Comment comment = fieldDeclaration.getComment().get();
        final String commentContent = comment.getContent();
        String info = commentContent.split("\n")[1].replace("*", "").trim();
        if (info.startsWith("@") || info.startsWith("{@")) {
            return BLANK;
        }
        return info;
    }

    public static String javadocFirstLineOf(MethodDeclaration methodDeclaration) {
        if (!methodDeclaration.getComment().isPresent()) {
            return BLANK;
        }

        final Comment comment = methodDeclaration.getComment().get();
        if (!"JavadocComment".equals(comment.getClass().getSimpleName()) || comment.getContent().isEmpty()) {
            // respect javadoc only
            return BLANK;
        }

        final String commentContent = comment.getContent();
        // 取注释的第一句话
        String info = commentContent.split("\n")[1].replace("*", "").trim();
        if (info.startsWith("@") || info.startsWith("{@")) {
            // javadoc上没有写方法描述，直接就写 @return 或 @param 等
            // {@inheritDoc}
            return BLANK;
        }
        return info;
    }

    public static String javadocFirstLineOf(ClassOrInterfaceDeclaration classOrInterfaceDeclaration) {
        if (!classOrInterfaceDeclaration.getComment().isPresent()) {
            return BLANK;
        }

        final Comment comment = classOrInterfaceDeclaration.getComment().get();
        if (!"JavadocComment".equals(comment.getClass().getSimpleName()) || comment.getContent().isEmpty()) {
            // respect javadoc only
            return BLANK;
        }

        final String commentContent = comment.getContent();
        // 取注释的第一句话
        String info = commentContent.split("\n")[1].replace("*", "").trim();
        // 由于IDEA有热键自动生成javadoc，里面自动携带类名：冗余信息，统统去掉
        info = info.replaceAll(classOrInterfaceDeclaration.getNameAsString(), "").trim();
        if (info.startsWith("@") || info.startsWith("{@")) {
            // javadoc上没有写方法描述，直接就写 @return 或 @param 等
            // {@inheritDoc}
            return BLANK;
        }

        return info;
    }
}
