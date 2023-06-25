/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import io.github.dddplus.ast.report.ClassMethodReport;
import io.github.dddplus.ast.parser.JavaParserUtil;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Generated;
import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

class ClassMethodDistributionAstNodeVisitor extends VoidVisitorAdapter<ClassMethodReport> {
    private static Set<String> ignoredMethodNames = new HashSet<>();
    private static Set<Class> ignoredMethodAnnotation = new HashSet<>();
    private static Set<Class> ignoredClassAnnotation = new HashSet<>();
    static {
        //ignoredMethodNames.add("of");
        //ignoredMethodNames.add("isSatisfiedBy");

        ignoredMethodAnnotation.add(Autowired.class);
        ignoredMethodAnnotation.add(Resource.class);

        ignoredClassAnnotation.add(Generated.class); // MapStruct generated mapper impl ignored
    }

    @Override
    public void visit(final MethodDeclaration methodDeclaration, final ClassMethodReport report) {
        super.visit(methodDeclaration, report);

        if (ignoredMethodNames.contains(methodDeclaration.getNameAsString())) {
            return;
        }

        for (Class annotation : ignoredMethodAnnotation) {
            if (methodDeclaration.getAnnotationByClass(annotation).isPresent()) {
                return;
            }
        }

        ClassOrInterfaceDeclaration parentClass = JavaParserUtil.getClass(methodDeclaration.getParentNode().get());
        if (parentClass == null) {
            // 它可能是 enum 里的 public method, ignored
            return;
        }
        if (parentClass.isEnumDeclaration() || parentClass.isInterface() || parentClass.isAbstract()) {
            // enum, interface, abstract class 不统计
            return;
        }

        for (Class annotation : ignoredClassAnnotation) {
            if (parentClass.getAnnotationByClass(annotation).isPresent()) {
                return;
            }
        }

        // method features
        ClassMethodReport.MethodInfo methodInfo = report.getMethodInfo();
        final String className = parentClass.getFullyQualifiedName().get();
        final String methodName = className + "#" + methodDeclaration.getNameAsString();
        if (methodDeclaration.isAnnotationPresent(Deprecated.class)) {
            methodInfo.getDeprecatedMethods().add(methodName);
        }
        if (methodDeclaration.isPublic()) {
            methodInfo.getPublicMethods().add(methodName);
        }
        if (methodDeclaration.isAbstract()) {
            methodInfo.getAbstractMethods().add(methodName);
        }
        if (methodDeclaration.isProtected()) {
            methodInfo.getProtectedMethods().add(methodName);
        }
        if (methodDeclaration.isPrivate()) {
            methodInfo.getPrivateMethods().add(methodName);
        }
        if (methodDeclaration.isStatic()) {
            methodInfo.getStaticMethods().add(methodName);
        }
        if (methodDeclaration.getModifiers().isEmpty()) {
            // methodDeclaration.isDefault() 表示 java interface default，而不是默认的可见性
            methodInfo.getDefaultMethods().add(methodName);
        }

        // class features
        ClassMethodReport.ClassInfo classInfo = report.getClassInfo();
        if (parentClass.isAnnotationPresent(Deprecated.class)) {
            classInfo.getDeprecatedClasses().add(className);
        }
        if (parentClass.isPublic()) {
            classInfo.getPublicClasses().add(className);
        }
        if (parentClass.isInnerClass()) {
            classInfo.getInnerClasses().add(className);
        }
        if (parentClass.isGeneric()) {
            classInfo.getGenericClasses().add(className);
        }
        if (parentClass.isAbstract()) {
            classInfo.getAbstractClasses().add(className);
        }
        if (parentClass.isInterface()) {
            classInfo.getInterfaces().add(className);
        }
    }

}
