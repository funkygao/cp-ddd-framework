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
import io.github.dddplus.ast.model.KeyUsecaseEntry;
import io.github.dddplus.ast.parser.JavaParserUtil;
import io.github.dddplus.ast.parser.KeyUsecaseAnnotationParser;
import io.github.dddplus.ast.report.KeyUsecaseReport;
import io.github.dddplus.dsl.KeyUsecase;

import java.util.Set;

class KeyUsecaseAstNodeVisitor extends VoidVisitorAdapter<KeyUsecaseReport> {
    private final Set<String> ignoredAnnotations;

    public KeyUsecaseAstNodeVisitor(Set<String> ignoredAnnotations) {
        this.ignoredAnnotations = ignoredAnnotations;
    }

    @Override
    public void visit(final MethodDeclaration methodDeclaration, final KeyUsecaseReport report) {
        super.visit(methodDeclaration, report);

        if (!methodDeclaration.isAnnotationPresent(KeyUsecase.class)) {
            return;
        }

        if (methodDeclaration.isAnnotationPresent(Deprecated.class)) {
            System.out.printf("SKIP: @KeyUsecase used on Deprecated %s\n", methodDeclaration.getNameAsString());
            return;
        }

        for (String annotation : ignoredAnnotations) {
            if (methodDeclaration.isAnnotationPresent(annotation)) {
                System.out.printf("SKIP: @KeyUsecase used on %s %s\n", annotation, methodDeclaration.getNameAsString());
                return;
            }
        }

        ClassOrInterfaceDeclaration parentClass = JavaParserUtil.getClass(methodDeclaration.getParentNode().get());
        if (parentClass == null) {
            return;
        }

        final String className = parentClass.getNameAsString();
        AnnotationExpr annotationExpr = methodDeclaration.getAnnotationByClass(KeyUsecase.class).get();
        KeyUsecaseEntry entry = new KeyUsecaseAnnotationParser(methodDeclaration, className).parse(annotationExpr);
        report.register(entry);
        if (report.actorJavadoc(className) == null) {
            report.registerActorJavadoc(className, JavaParserUtil.javadocFirstLineOf(parentClass));
        }
    }

}
