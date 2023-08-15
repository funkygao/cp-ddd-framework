/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import io.github.dddplus.ast.model.KeyRuleEntry;
import io.github.dddplus.ast.parser.JavaParserUtil;
import io.github.dddplus.ast.parser.KeyRuleAnnotationParser;
import io.github.dddplus.ast.report.KeyRuleReport;
import io.github.dddplus.dsl.KeyRule;
import io.github.dddplus.model.encapsulation.AllowedAccessors;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

class KeyRuleAstNodeVisitor extends VoidVisitorAdapter<KeyRuleReport> {
    private final Set<String> ignoredAnnotations;
    private static final List<String> javaPrimitiveTypes = Arrays.asList(
            "boolean",
            "date",
            "string",
            "byte", "char",
            "integer", "int", "long", "short", "bigdecimal", "double", "float"
    );

    public KeyRuleAstNodeVisitor(Set<String> ignoredAnnotations) {
        this.ignoredAnnotations = ignoredAnnotations;
    }

    @Override
    public void visit(final MethodDeclaration methodDeclaration, final KeyRuleReport report) {
        super.visit(methodDeclaration, report);

        if (!methodDeclaration.isAnnotationPresent(KeyRule.class)) {
            // boolean foo(), auto register KeyRule
            if (methodDeclaration.isPublic()
                    && !methodDeclaration.isAnnotationPresent(Deprecated.class)
                    && (methodDeclaration.getParameters() == null || methodDeclaration.getParameters().size() == 0)
                    && isJavaPrimitiveType(methodDeclaration.getTypeAsString())) {
                if (methodDeclaration.isAnnotationPresent(AllowedAccessors.class)) {
                    // 指定类可访问的方法，忽略
                    return;
                }

                ClassOrInterfaceDeclaration parentClass = JavaParserUtil.getClass(methodDeclaration.getParentNode().get());
                if (parentClass == null) {
                    return;
                }

                KeyRuleEntry entry = new KeyRuleEntry();
                entry.setClassName(parentClass.getNameAsString());
                entry.setMethodName(methodDeclaration.getNameAsString());
                entry.setRealMethodName(entry.getMethodName());
                entry.setJavadoc(JavaParserUtil.javadocFirstLineOf(methodDeclaration));
                report.register(entry);
            }

            return;
        }

        if (methodDeclaration.isAnnotationPresent(Deprecated.class)) {
            System.out.printf("SKIP: @KeyRule used on Deprecated %s\n", methodDeclaration.getNameAsString());
            return;
        }

        for (String annotation : ignoredAnnotations) {
            if (methodDeclaration.isAnnotationPresent(annotation)) {
                System.out.printf("SKIP: @KeyRule used on %s %s\n", annotation, methodDeclaration.getNameAsString());
                return;
            }
        }

        ClassOrInterfaceDeclaration parentClass = JavaParserUtil.getClass(methodDeclaration.getParentNode().get());
        if (parentClass == null) {
            return;
        }

        final String className = parentClass.getNameAsString();
        AnnotationExpr annotationExpr = methodDeclaration.getAnnotationByClass(KeyRule.class).get();
        KeyRuleEntry entry = new KeyRuleAnnotationParser(methodDeclaration, className).parse(annotationExpr);
        report.register(entry);
    }

    private boolean isJavaPrimitiveType(String typeClass) {
        return javaPrimitiveTypes.contains(typeClass.toLowerCase());
    }

}
