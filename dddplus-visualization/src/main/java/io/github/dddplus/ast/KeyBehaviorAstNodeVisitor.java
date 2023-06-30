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
import io.github.dddplus.ast.model.KeyBehaviorEntry;
import io.github.dddplus.ast.parser.JavaParserUtil;
import io.github.dddplus.ast.parser.KeyBehaviorAnnotationParser;
import io.github.dddplus.ast.report.KeyBehaviorReport;
import io.github.dddplus.dsl.KeyBehavior;

import java.util.Set;

class KeyBehaviorAstNodeVisitor extends VoidVisitorAdapter<KeyBehaviorReport> {
    private final Set<String> ignoredAnnotations;

    public KeyBehaviorAstNodeVisitor(Set<String> ignoredAnnotations) {
        this.ignoredAnnotations = ignoredAnnotations;
    }

    @Override
    public void visit(final MethodDeclaration methodDeclaration, final KeyBehaviorReport report) {
        super.visit(methodDeclaration, report);

        if (!methodDeclaration.isAnnotationPresent(KeyBehavior.class)) {
            return;
        }

        if (methodDeclaration.isAnnotationPresent(Deprecated.class)) {
            System.out.printf("SKIP: @KeyBehavior used on Deprecated %s\n", methodDeclaration.getNameAsString());
            return;
        }

        for (String annotation : ignoredAnnotations) {
            if (methodDeclaration.isAnnotationPresent(annotation)) {
                System.out.printf("SKIP: @KeyBehavior used on %s %s\n", annotation, methodDeclaration.getNameAsString());
                return;
            }
        }

        ClassOrInterfaceDeclaration parentClass = JavaParserUtil.getClass(methodDeclaration.getParentNode().get());
        if (parentClass == null) {
            return;
        }

        final String packageName = JavaParserUtil.packageName(parentClass);
        final String className = parentClass.getNameAsString();
        AnnotationExpr annotationExpr = methodDeclaration.getAnnotationByClass(KeyBehavior.class).get();
        KeyBehaviorEntry entry = new KeyBehaviorAnnotationParser(methodDeclaration, className, packageName).parse(annotationExpr);
        report.register(entry);
    }

}
