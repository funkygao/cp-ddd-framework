/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import io.github.dddplus.ast.model.AccessorsEntry;
import io.github.dddplus.ast.parser.AccessorsAnnotationParser;
import io.github.dddplus.ast.parser.JavaParserUtil;
import io.github.dddplus.model.encapsulation.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
class AccessorsAstNodeVisitor extends VoidVisitorAdapter<Void> {
    final List<AccessorsEntry> accessorsEntries = new ArrayList<>();
    boolean parseMethodDeclaration = false;

    public void visit(final MethodDeclaration methodDeclaration, final Void __) {
        super.visit(methodDeclaration, __);

        if (!parseMethodDeclaration) {
            return;
        }

        if (!methodDeclaration.isAnnotationPresent(Accessors.class)) {
            return;
        }

        ClassOrInterfaceDeclaration classOrInterfaceDeclaration = JavaParserUtil.getClass(methodDeclaration);
        AnnotationExpr annotationExpr = methodDeclaration.getAnnotationByClass(Accessors.class).get();
        AccessorsEntry entry = new AccessorsAnnotationParser(classOrInterfaceDeclaration, methodDeclaration)
                .parse(annotationExpr);
        accessorsEntries.add(entry);
    }

    @Override
    public void visit(final MethodCallExpr methodCallExpr, final Void __) {
        super.visit(methodCallExpr, __);

        if (parseMethodDeclaration) {
            return;
        }

        final String methodName = methodCallExpr.getName().asString();
        Optional<AccessorsEntry> accessorsEntry = findAccessorsEntry(methodName);
        if (!accessorsEntry.isPresent()) {
            return;
        }

        ClassOrInterfaceDeclaration accessorClazz = JavaParserUtil.getAccessor(methodCallExpr);
        if (accessorClazz == null) {
            log.error("method:{} cannot find accessor class", methodName);
            return;
        }

        // Order.setFoo 被标注为 @Accessors(IOrderRepository.class)
        // FIXME 但调用setFoo是IOrderRepository的实现类：OrderRepository
        final String accessorClassName = accessorClazz.getNameAsString();
        log.debug("{} calls {}", accessorClassName, methodCallExpr.getNameAsString());

        if (accessorsEntry.get().satisfy(accessorClassName)) {
            throw new RuntimeException(String.format("%s is not allowed for %s", methodName, accessorClassName));
        }
    }

    private Optional<AccessorsEntry> findAccessorsEntry(String methodName) {
        for (AccessorsEntry entry : accessorsEntries) {
            if (entry.getMethodName().equals(methodName)) {
                return Optional.of(entry);
            }
        }
        return Optional.empty();
    }

}
