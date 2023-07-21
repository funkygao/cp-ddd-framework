/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast.enforcer;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import io.github.dddplus.ast.DomainModelAnalyzer;
import io.github.dddplus.ast.FileWalker;
import io.github.dddplus.ast.model.AccessorsEntry;
import io.github.dddplus.ast.parser.AccessorsAnnotationParser;
import io.github.dddplus.ast.parser.JavaParserUtil;
import io.github.dddplus.model.encapsulation.AllowedAccessors;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 确保代码符合{@link AllowedAccessors}规范.
 */
public class AllowedAccessorsEnforcer {
    private File[] dirs;

    public AllowedAccessorsEnforcer scan(File... dirs) {
        this.dirs = dirs;
        return this;
    }

    public void enforce(FileWalker.Filter filter) {
        FileWalker.Filter actualFilter = new DomainModelAnalyzer.ActualFilter(filter);
        for (File dir : dirs) {
            AccessorsAstNodeVisitor accessorsAstNodeVisitor = new AccessorsAstNodeVisitor();

            // 1. parse annotated methods
            accessorsAstNodeVisitor.parseMethodDeclaration = true;
            new FileWalker(actualFilter, (level, path, file) -> {
                accessorsAstNodeVisitor.visit(FileWalker.silentParse(file), null);
            }).walkFrom(dir);

            // 2. parse method calls
            accessorsAstNodeVisitor.parseMethodDeclaration = false;
            new FileWalker(actualFilter, (level, path, file) -> {
                accessorsAstNodeVisitor.visit(FileWalker.silentParse(file), null);
            }).walkFrom(dir);
        }
    }

    @Slf4j
    static class AccessorsAstNodeVisitor extends VoidVisitorAdapter<Void> {
        final List<AccessorsEntry> accessorEntries = new ArrayList<>();
        boolean parseMethodDeclaration = false;

        public void visit(final MethodDeclaration methodDeclaration, final Void __) {
            super.visit(methodDeclaration, __);

            if (!parseMethodDeclaration) {
                return;
            }

            if (!methodDeclaration.isAnnotationPresent(AllowedAccessors.class)) {
                return;
            }

            ClassOrInterfaceDeclaration classOrInterfaceDeclaration = JavaParserUtil.getClass(methodDeclaration);
            AnnotationExpr annotationExpr = methodDeclaration.getAnnotationByClass(AllowedAccessors.class).get();
            AccessorsEntry entry = new AccessorsAnnotationParser(classOrInterfaceDeclaration, methodDeclaration)
                    .parse(annotationExpr);
            accessorEntries.add(entry);
        }

        @Override
        public void visit(final MethodCallExpr methodCallExpr, final Void __) {
            super.visit(methodCallExpr, __);

            if (parseMethodDeclaration) {
                return;
            }

            final String methodName = methodCallExpr.getName().asString();
            List<AccessorsEntry> possibleEntries = findPossibleAccessorsEntries(methodName);
            if (possibleEntries.isEmpty()) {
                return;
            }

            final String methodCallFullStmt = methodCallExpr.getNameAsString();
            if (methodCallFullStmt.startsWith("this.")) {
                // 自己定义的方法，自己可以访问
                return;
            }
            if (methodCallFullStmt.equals(methodName)) {
            /*
             * 自己调自己的方法, OK
             *
             * class Foo {
             *     IBar bar;
             *
             *     void a() {
             *     }
             *
             *     void b() {
             *         a(); //methodCallFullStmt: a, methodName: a
             *     }
             *
             *     void c() {
             *         bar.egg(); // methodCallFullStmt: bar.egg, methodName: egg
             *
             *     }
             * }
             */
                return;
            }

            ClassOrInterfaceDeclaration accessorClazz = methodCallExpr.findAncestor(ClassOrInterfaceDeclaration.class).orElse(null);
            if (accessorClazz == null) {
                EnumDeclaration accessorEnum = methodCallExpr.findAncestor(EnumDeclaration.class).get();
                log.debug("method:{} called by enum:{}", methodName, accessorEnum.getNameAsString());
                return;
            }

            // Order.setFoo 被标注为 @AllowedAccessors(IOrderRepository.class)
            // 但调用setFoo是IOrderRepository的实现类：OrderRepository
            final String accessorClassName = accessorClazz.getNameAsString();
            boolean satisfied = false;
            for (AccessorsEntry entry : possibleEntries) {
                if (entry.satisfy(accessorClazz)) {
                    satisfied = true;
                    break;
                }
            }

            if (!satisfied) {
                throw new RuntimeException(String.format("%s is not allowed for %s", methodName, accessorClassName));
            }
        }

        private List<AccessorsEntry> findPossibleAccessorsEntries(String methodName) {
            return accessorEntries.stream().filter(
                    entry -> entry.getDeclaredMethodName().equals(methodName))
                    .collect(Collectors.toList());
        }

    }
}
