/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import io.github.dddplus.ast.parser.JavaParserUtil;
import io.github.dddplus.ast.report.EncapsulationReport;
import io.github.dddplus.model.*;
import io.github.dddplus.model.encapsulation.AllowedAccessors;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Generated;
import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class PublicMethodAstNodeVisitor extends VoidVisitorAdapter<EncapsulationReport> {
    private static Set<String> ignoredMethodNames = new HashSet<>();
    private static Set<Class> ignoredMethodAnnotation = new HashSet<>();
    private static Set<Class> ignoredClassAnnotation = new HashSet<>();
    private static Set<Class> ignoredClassContract = new HashSet<>();

    static {
        ignoredMethodNames.add("of");
        ignoredMethodNames.add("xGet");
        ignoredMethodNames.add("xIs");
        ignoredMethodNames.add("xSet");
        ignoredMethodNames.add("isSatisfiedBy");
        ignoredMethodNames.add("validate");
        ignoredMethodNames.add("inject");
        ignoredMethodNames.add("getId");
        ignoredMethodNames.add("onMessage");
        ignoredMethodNames.add("isOk");
        ignoredMethodNames.add("setSuccessor");
        ignoredMethodNames.add("processEvent");

        ignoredClassContract.add(IDirtyHint.class);
        ignoredClassContract.add(IMergeAwareDirtyHint.class);
        ignoredClassContract.add(IUnitOfWork.class);
        ignoredClassContract.add(IApplicationService.class);
        ignoredClassContract.add(INativeFlow.class);

        ignoredMethodAnnotation.add(Deprecated.class);
        ignoredMethodAnnotation.add(Resource.class);

        ignoredClassAnnotation.add(Deprecated.class);
        ignoredClassAnnotation.add(Generated.class); // MapStruct generated mapper impl ignored
    }

    @Override
    public void visit(final MethodDeclaration methodDeclaration, final EncapsulationReport report) {
        super.visit(methodDeclaration, report);

        ClassOrInterfaceDeclaration parentClass = JavaParserUtil.getClass(methodDeclaration.getParentNode().get());
        if (parentClass == null) {
            log.warn("method: {} is not declared inside class", methodDeclaration.getNameAsString());
            return;
        }

        String className = parentClass.getNameAsString();

        if (!JavaParserUtil.isMethodPublic(parentClass, methodDeclaration)
                || skipMethod(methodDeclaration, parentClass)) {
            return;
        }

        StringBuilder methodInfo = new StringBuilder(methodDeclaration.getNameAsString());
        final String comment = JavaParserUtil.javadocFirstLineOf(methodDeclaration);
        if (!comment.isEmpty()) {
            methodInfo.append(": ").append(comment);
        }
        String javadoc = JavaParserUtil.javadocFirstLineOf(parentClass);
        if (!javadoc.isEmpty()) {
            className = className + " " + javadoc;
        }
        report.registerMethodInfo(className, methodInfo.toString());
    }

    private boolean skipMethod(MethodDeclaration methodDeclaration, ClassOrInterfaceDeclaration parentClass) {
        if (methodDeclaration.isAnnotationPresent(AllowedAccessors.class)) {
            // 指定类可访问的方法，忽略
            return true;
        }

        for (String ignoredPrefix : ignoredMethodNames) {
            if (methodDeclaration.getNameAsString().startsWith(ignoredPrefix)) {
                return true;
            }
        }

        for (Class annotation : ignoredMethodAnnotation) {
            if (methodDeclaration.getAnnotationByClass(annotation).isPresent()) {
                return true;
            }
        }

        for (Class annotation : ignoredClassAnnotation) {
            if (parentClass.getAnnotationByClass(annotation).isPresent()) {
                return true;
            }
        }

        for (Class ignoredContract : ignoredClassContract) {
            if (JavaParserUtil.implementsInterface(parentClass, ignoredContract)) {
                return true;
            }
        }

        return false;
    }

}
