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
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import io.github.dddplus.ast.parser.JavaParserUtil;
import io.github.dddplus.ast.report.CallGraphReport;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

@Slf4j
class CallGraphAstNodeVisitor extends VoidVisitorAdapter<CallGraphReport> {
    private final JavaParserFacade javaParserFacade;

    CallGraphAstNodeVisitor(File[] dirs) {
        // as we need to resolve the declaration class from MethodCallExpr, we need setup the symbol resolver
        // 常见的类型推断：JavaParserTypeSolver、ReflectionTypeSolver、
        CombinedTypeSolver typeSolver = new CombinedTypeSolver(new ReflectionTypeSolver(false));
        for (File dir : dirs) {
            typeSolver.add(new JavaParserTypeSolver(dir));
        }
        javaParserFacade = JavaParserFacade.get(typeSolver);
    }

    @Override
    public void visit(final MethodCallExpr methodCallExpr, final CallGraphReport report) {
        super.visit(methodCallExpr, report);

        final String methodName = methodCallExpr.getName().asString();
        Expression scope = methodCallExpr.getScope().orElse(null);
        if (scope == null) {
            // 自己用自己，不出现在call graph
            return;
        }

        SymbolReference<ResolvedMethodDeclaration> methodDeclaration = javaParserFacade.solve(methodCallExpr);
        if (!methodDeclaration.isSolved()) {
            log.error("method {} cannot be solved", methodCallExpr.getNameAsString());
            return;
        }

        String resolvedTypeDescribe = methodDeclaration.getCorrespondingDeclaration()
                .declaringType().getClassName();
        // 如果调用的是内部类的方法，例如：Order.OrderTasks.foo()，此时 resolvedTypeDescribe=Order.OrderTasks
        // 这里做裁剪：Order.OrderTasks -> OrderTasks
        String finalDeclarationClazz = JavaParserUtil.resolvedTypeAsString(resolvedTypeDescribe);
        if (!report.interestedInMethod(finalDeclarationClazz, methodName)) {
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

        report.register(accessorClassName, accessorMethod.getNameAsString(), finalDeclarationClazz, methodName);
    }

}
