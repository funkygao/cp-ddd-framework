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
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

/**
 * Java AST Node 工具.
 */
public final class JavaParserUtil {
    private static final String BLANK = "";
    private static final Set<String> emptySet = new HashSet<>();
    private static final Set<String> ignoredArgument = new HashSet<>();
    static {
        ignoredArgument.add("String");
        ignoredArgument.add("Boolean");
        ignoredArgument.add("boolean");
        ignoredArgument.add("Integer");
        ignoredArgument.add("int");
        ignoredArgument.add("Date");
    }

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
        return implementsInterface(classOrInterfaceDeclaration, theInterface.getSimpleName());
    }

    public static boolean implementsInterface(ClassOrInterfaceDeclaration classOrInterfaceDeclaration, String interfaceSimpleName) {
        for (ClassOrInterfaceType t : classOrInterfaceDeclaration.getImplementedTypes()) {
            if (t.getNameAsString().equals(interfaceSimpleName)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isMethodPublic(ClassOrInterfaceDeclaration classOrInterfaceDeclaration, MethodDeclaration methodDeclaration) {
        if (methodDeclaration.isPublic()) {
            return true;
        }

        if (classOrInterfaceDeclaration.isInterface() && methodDeclaration.isDefault()) {
            return true;
        }

        return false;
    }

    public static ClassOrInterfaceDeclaration getAccessor(MethodCallExpr methodCallExpr) {
        Optional<Node> parentNode = methodCallExpr.getParentNode();
        while (true) {
            if (!parentNode.isPresent()) {
                return null;
            }

            if (parentNode.get() instanceof ClassOrInterfaceDeclaration) {
                return (ClassOrInterfaceDeclaration) parentNode.get();
            }

            parentNode = parentNode.get().getParentNode();
        }
    }

    static Set<String> extractMethodArguments(MethodDeclaration methodDeclaration) {
        if (methodDeclaration.getParameters() == null) {
            return emptySet;
        }

        Set<String> realArguments = new TreeSet<>();
        for (Parameter parameter : methodDeclaration.getParameters()) {
            if (!ignoredArgument.contains(parameter.getTypeAsString())) {
                realArguments.add(parameter.getTypeAsString());
            }
        }
        return realArguments;
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
        info = info.replaceAll("\\{@link ", "").replaceAll("\\}", "");
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
