/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.types.ResolvedType;
import io.github.dddplus.ast.parser.JavaParserUtil;
import io.github.dddplus.ast.report.CallGraphReport;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class CallGraphAstNodeVisitor extends VoidVisitorAdapter<CallGraphReport> {

    @Override
    public void visit(final MethodCallExpr methodCallExpr, final CallGraphReport report) {
        super.visit(methodCallExpr, report);

        final String methodName = methodCallExpr.getName().asString();
        Expression scope = methodCallExpr.getScope().orElse(null);
        if (scope == null) {
            // 自己调用自己，不出现在call graph
            return;
        }

        ResolvedType resolvedType = scope.calculateResolvedType();
        String declarationClazz = JavaParserUtil.resolvedTypeAsString(resolvedType.describe());
        if (!report.interestedInMethod(declarationClazz, methodName)) {
            return;
        }

        MethodDeclaration accessorMethod = methodCallExpr.findAncestor(MethodDeclaration.class).get();
        ClassOrInterfaceDeclaration accessorClazz = accessorMethod.findAncestor(ClassOrInterfaceDeclaration.class).orElse(null);
        String accessorClassName;
        if (accessorClazz == null) {
            EnumDeclaration enumDeclaration = accessorMethod.findAncestor(EnumDeclaration.class).get();
            accessorClassName = enumDeclaration.getNameAsString();
        } else {
            accessorClassName = accessorClazz.getNameAsString();
        }

        report.register(accessorClassName, accessorMethod.getNameAsString(), declarationClazz, methodName);
    }

}
