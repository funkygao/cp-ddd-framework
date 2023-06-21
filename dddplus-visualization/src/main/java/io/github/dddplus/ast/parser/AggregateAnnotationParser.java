/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast.parser;

import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.expr.*;
import io.github.dddplus.ast.model.AggregateEntry;
import lombok.Getter;

/**
 * {@link io.github.dddplus.dsl.Aggregate}
 */
@Getter
public class AggregateAnnotationParser {
    private final PackageDeclaration packageDeclaration;

    public AggregateAnnotationParser(PackageDeclaration packageDeclaration) {
        this.packageDeclaration = packageDeclaration;
    }

    public AggregateEntry parse(AnnotationExpr aggregateAnnotationExpr) {
        AggregateEntry entry = new AggregateEntry();
        entry.setPackageName(packageDeclaration.getNameAsString());
        NormalAnnotationExpr expr = (NormalAnnotationExpr) aggregateAnnotationExpr;
        for (MemberValuePair memberValuePair : expr.getPairs()) {
            switch (memberValuePair.getNameAsString()) {
                case "name":
                    StringLiteralExpr nameExpr = (StringLiteralExpr) memberValuePair.getValue();
                    entry.setName(nameExpr.getValue());
                    break;

                case "root":
                    // FIXME is Class[]
                    ClassExpr classExpr = (ClassExpr) memberValuePair.getValue();
                    entry.setRootClass(classExpr.getTypeAsString());
                    break;
            }
        }

        return entry;
    }

}
