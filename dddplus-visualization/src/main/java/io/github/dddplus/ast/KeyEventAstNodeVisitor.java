/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import io.github.dddplus.ast.model.KeyEventEntry;
import io.github.dddplus.ast.parser.KeyEventAnnotationParser;
import io.github.dddplus.ast.report.KeyEventReport;
import io.github.dddplus.dsl.KeyEvent;

class KeyEventAstNodeVisitor extends VoidVisitorAdapter<KeyEventReport> {

    @Override
    public void visit(final ClassOrInterfaceDeclaration classDeclaration, final KeyEventReport report) {
        super.visit(classDeclaration, report);

        if (!classDeclaration.isAnnotationPresent(KeyEvent.class)) {
            return;
        }

        if (classDeclaration.isAnnotationPresent(Deprecated.class)) {
            System.out.printf("SKIP: @KeyEvent used on Deprecated %s\n", classDeclaration.getNameAsString());
            return;
        }

        AnnotationExpr annotationExpr = classDeclaration.getAnnotationByClass(KeyEvent.class).get();
        KeyEventEntry entry = new KeyEventAnnotationParser(classDeclaration).parse(annotationExpr);
        report.register(entry);
    }

}
