/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.MethodReferenceExpr;
import com.github.javaparser.ast.expr.TypeExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.model.SymbolReference;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import io.github.dddplus.ast.parser.JavaParserUtil;
import io.github.dddplus.ast.report.CallGraphReport;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Optional;

@Slf4j
class CallGraphAstNodeVisitor extends VoidVisitorAdapter<CallGraphReport> {
    private final JavaParserFacade javaParserFacade;

    CallGraphAstNodeVisitor(File[] dirs) {
        // as we need to resolve the declaration class from MethodCallExpr, we need setup the symbol resolver
        // 常见的类型推断：JavaParserTypeSolver、ReflectionTypeSolver、
        CombinedTypeSolver typeSolver = new CombinedTypeSolver(new ReflectionTypeSolver(false));
        for (File dir : dirs) {
            // JavaParserTypeSolver looks for classes defined in source files
            typeSolver.add(new JavaParserTypeSolver(dir));
        }
        javaParserFacade = JavaParserFacade.get(typeSolver);
    }

    @Override
    public void visit(final MethodReferenceExpr methodReferenceExpr, final CallGraphReport report) {
        super.visit(methodReferenceExpr, report);

        if (!(methodReferenceExpr.getScope() instanceof TypeExpr)) {
            return;
        }

        Optional<Node> parentNode = methodReferenceExpr.getParentNode();
        if (!parentNode.isPresent() || !(parentNode.get() instanceof MethodCallExpr)) {
            return;
        }

        MethodCallExpr methodCallExpr = (MethodCallExpr) parentNode.get();
        MethodDeclaration accessorMethod = methodCallExpr.findAncestor(MethodDeclaration.class).get();
        ClassOrInterfaceDeclaration accessorClazz = accessorMethod.findAncestor(ClassOrInterfaceDeclaration.class).orElse(null);
        if (accessorClazz == null) {
            return;
        }

        // chained method invocation
        // items.stream().map(OrderLine::expectedQty).reduce(BigDecimal.ZERO, BigDecimal::add)
        final String methodName = methodReferenceExpr.getIdentifier();
        final TypeExpr declaredClazz = (TypeExpr) methodReferenceExpr.getScope();

        report.register(accessorClazz.getNameAsString(), accessorMethod.getNameAsString(),
                declaredClazz.getTypeAsString(), methodName);

        final String callerPackage = JavaParserUtil.packageName(accessorClazz);
        ResolvedMethodDeclaration calleeMethodDeclaration = javaParserFacade.solve(methodReferenceExpr).getCorrespondingDeclaration();
        report.addPackageCrossRef(callerPackage, calleeMethodDeclaration.getPackageName());
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

        SymbolReference<ResolvedMethodDeclaration> methodDeclaration;
        try {
            methodDeclaration = javaParserFacade.solve(methodCallExpr, true);
        } catch (Exception ignored) {
            log.warn("method:{} {}", methodCallExpr.getNameAsString(), ignored.getMessage());
            return;
        }

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

        report.register(accessorClassName, accessorMethod.getNameAsString(),
                finalDeclarationClazz, methodName);

        String calleePackage = methodDeclaration.getCorrespondingDeclaration()
                .getPackageName();
        String callerPackage = JavaParserUtil.packageName(accessorClazz);
        if (!callerPackage.equals(calleePackage)) {
            report.addPackageCrossRef(callerPackage, calleePackage);
        }
    }

}
