/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import io.github.dddplus.ast.model.KeyFlowEntry;
import io.github.dddplus.ast.parser.JavaParserUtil;
import io.github.dddplus.ast.parser.KeyFlowAnnotationParser;
import io.github.dddplus.ast.report.KeyFlowReport;
import io.github.dddplus.dsl.KeyFlow;

import java.util.Set;

class KeyFlowAstNodeVisitor extends VoidVisitorAdapter<KeyFlowReport> {
    private final Set<String> ignoredAnnotations;

    public KeyFlowAstNodeVisitor(Set<String> ignoredAnnotations) {
        this.ignoredAnnotations = ignoredAnnotations;
    }

    @Override
    public void visit(final MethodDeclaration methodDeclaration, final KeyFlowReport report) {
        super.visit(methodDeclaration, report);

        if (!methodDeclaration.isAnnotationPresent(KeyFlow.class)) {
            return;
        }

        if (!methodDeclaration.isPublic()) {
            // just warn
            System.out.printf("WARN: @KeyFlow used on non-public %s\n", methodDeclaration.getNameAsString());
        }

        for (String annotation : ignoredAnnotations) {
            if (methodDeclaration.isAnnotationPresent(annotation)) {
                System.out.printf("SKIP: @KeyFlow used on %s %s\n", annotation, methodDeclaration.getNameAsString());
                return;
            }
        }

        if (methodDeclaration.isAnnotationPresent(Deprecated.class)) {
            System.out.printf("SKIP: @KeyFlow used on Deprecated %s\n", methodDeclaration.getNameAsString());
            return;
        }

        ClassOrInterfaceDeclaration parentClass = JavaParserUtil.getClass(methodDeclaration.getParentNode().get());
        if (parentClass == null) {
            return;
        }

        final String className = parentClass.getNameAsString();
        AnnotationExpr annotationExpr = methodDeclaration.getAnnotationByClass(KeyFlow.class).get();
        KeyFlowEntry entry = new KeyFlowAnnotationParser(methodDeclaration, className).parse(annotationExpr);
        report.register(entry);
    }

}
