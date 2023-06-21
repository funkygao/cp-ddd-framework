/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import io.github.dddplus.ast.report.KeyEventReport;
import io.github.dddplus.dsl.KeyEvent;

public class KeyEventAstNodeVisitor extends VoidVisitorAdapter<KeyEventReport> {

    @Override
    public void visit(final ClassOrInterfaceDeclaration classDeclaration, final KeyEventReport report) {
        super.visit(classDeclaration, report);

        if (!classDeclaration.isAnnotationPresent(KeyEvent.class)) {
            return;
        }

        if (classDeclaration.isAnnotationPresent(Deprecated.class)) {
            System.out.printf("WARN: @KeyEvent used on Deprecated %s\n", classDeclaration.getNameAsString());
            return;
        }
    }

}
