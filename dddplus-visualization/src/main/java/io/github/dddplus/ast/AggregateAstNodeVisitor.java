/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast;

import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import io.github.dddplus.ast.model.AggregateEntry;
import io.github.dddplus.ast.report.AggregateReport;
import io.github.dddplus.ast.parser.AggregateAnnotationParser;
import io.github.dddplus.dsl.Aggregate;

class AggregateAstNodeVisitor extends VoidVisitorAdapter<AggregateReport> {
    @Override
    public void visit(final PackageDeclaration packageDeclaration, final AggregateReport report) {
        super.visit(packageDeclaration, report);

        if (!packageDeclaration.isAnnotationPresent(Aggregate.class)) {
            return;
        }

        AnnotationExpr annotationExpr = packageDeclaration.getAnnotationByClass(Aggregate.class).get();
        AggregateAnnotationParser parser = new AggregateAnnotationParser(packageDeclaration);
        AggregateEntry aggregateEntry = parser.parse(annotationExpr);
        report.add(aggregateEntry);
    }

}
