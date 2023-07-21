/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast.parser;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import io.github.dddplus.ast.model.AccessorsEntry;
import lombok.AllArgsConstructor;

import java.util.Set;

/**
 * {@link io.github.dddplus.model.encapsulation.AllowedAccessors}.
 */
@AllArgsConstructor
public class AccessorsAnnotationParser {
    private final ClassOrInterfaceDeclaration classOrInterfaceDeclaration;
    private final MethodDeclaration methodDeclaration;

    public AccessorsEntry parse(AnnotationExpr expr) {
        SingleMemberAnnotationExpr singleMemberAnnotationExpr = (SingleMemberAnnotationExpr) expr;
        Set<String> accessorClasses = AnnotationFieldParser.arrayFieldValue(singleMemberAnnotationExpr.getMemberValue());
        AccessorsEntry entry = new AccessorsEntry();
        entry.setAccessorsClasses(accessorClasses);
        entry.setDeclaredClassName(classOrInterfaceDeclaration.getNameAsString());
        entry.setDeclaredMethodName(methodDeclaration.getNameAsString());
        return entry;
    }

}
