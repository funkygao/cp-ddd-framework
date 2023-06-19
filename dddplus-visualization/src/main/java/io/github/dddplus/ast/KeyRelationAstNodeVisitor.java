/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import io.github.dddplus.ast.model.KeyRelationEntry;
import io.github.dddplus.ast.parser.KeyRelationAnnotationParser;
import io.github.dddplus.ast.report.KeyRelationReport;
import io.github.dddplus.dsl.KeyRelation;

public class KeyRelationAstNodeVisitor extends VoidVisitorAdapter<KeyRelationReport> {

    @Override
    public void visit(final ClassOrInterfaceDeclaration classDeclaration, final KeyRelationReport report) {
        super.visit(classDeclaration, report);

        if (!classDeclaration.isAnnotationPresent(KeyRelation.class)) {
            return;
        }

        if (classDeclaration.isAnnotationPresent(Deprecated.class)) {
            System.out.printf("WARN: @KeyRelation used on Deprecated %s\n", classDeclaration.getNameAsString());
            return;
        }

        // KeyRelation is repeatable
        for (AnnotationExpr annotationExpr : classDeclaration.getAnnotations()) {
            if (!annotationExpr.getNameAsString().equals(KeyRelation.class.getSimpleName())) {
                continue;
            }

            KeyRelationEntry entry = new KeyRelationAnnotationParser(classDeclaration).parse(annotationExpr);
            report.add(entry);
        }
    }

}
