/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast;

import com.github.javaparser.Range;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import io.github.dddplus.ast.model.KeyModelEntry;
import io.github.dddplus.ast.parser.JavaParserUtil;
import io.github.dddplus.ast.report.ClassMethodReport;
import io.github.dddplus.dsl.IVirtualModel;

import javax.annotation.Generated;
import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

class ClassMethodDistributionAstNodeVisitor extends VoidVisitorAdapter<ClassMethodReport> {
    private static Set<String> ignoredMethodNames = new HashSet<>();
    private static Set<Class> ignoredMethodAnnotation = new HashSet<>();
    private static Set<Class> ignoredClassAnnotation = new HashSet<>();
    static {
        ignoredMethodAnnotation.add(Resource.class);
        ignoredClassAnnotation.add(Generated.class); // MapStruct generated mapper impl ignored
    }

    @Override
    public void visit(final ClassOrInterfaceDeclaration classDeclaration, final ClassMethodReport report) {
        super.visit(classDeclaration, report);

        if (!JavaParserUtil.implementsInterface(classDeclaration, IVirtualModel.class)) {
            return;
        }

        // 注册该虚拟业务对象到Aggregate
        KeyModelEntry entry = report.getModel().getKeyModelReport()
                .getOrCreateKeyModelEntryForActor(classDeclaration.getNameAsString());
        entry.setPackageName(JavaParserUtil.packageName(classDeclaration));
        entry.setJavadoc(JavaParserUtil.javadocFirstLineOf(classDeclaration));
    }

    public void visit(final FieldDeclaration fieldDeclaration, final ClassMethodReport report) {
        super.visit(fieldDeclaration, report);
        if (fieldDeclaration.isAnnotationPresent(Deprecated.class)) {
            return;
        }

        report.incrField();
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
        Range range = methodDeclaration.getRange().orElse(null);
        if (range != null) {
            int methodLineOfCode = range.end.line - range.begin.line;
            methodInfo.getBigMethods().put(methodLineOfCode, methodName);
        }
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

    @Override
    public void visit(BlockStmt stmt, final ClassMethodReport report) {
        report.incrStatement();
    }
    @Override
    public void visit(BreakStmt stmt, final ClassMethodReport report) {
        report.incrStatement();
    }
    @Override
    public void visit(ContinueStmt stmt, final ClassMethodReport report) {
        report.incrStatement();
    }
    @Override
    public void visit(DoStmt stmt, final ClassMethodReport report) {
        report.incrStatement();
    }
    @Override
    public void visit(ExplicitConstructorInvocationStmt stmt, final ClassMethodReport report) {
        report.incrStatement();
    }
    @Override
    public void visit(ExpressionStmt stmt, final ClassMethodReport report) {
        report.incrStatement();
    }
    @Override
    public void visit(ForEachStmt stmt, final ClassMethodReport report) {
        report.incrStatement();
    }
    @Override
    public void visit(ForStmt stmt, final ClassMethodReport report) {
        report.incrStatement();
    }
    @Override
    public void visit(WhileStmt stmt, final ClassMethodReport report) {
        report.incrStatement();
    }
    @Override
    public void visit(IfStmt stmt, final ClassMethodReport report) {
        report.incrStatement();
    }
    @Override
    public void visit(LabeledStmt stmt, final ClassMethodReport report) {
        report.incrStatement();
    }
    @Override
    public void visit(ReturnStmt stmt, final ClassMethodReport report) {
        report.incrStatement();
    }
    @Override
    public void visit(SwitchStmt stmt, final ClassMethodReport report) {
        report.incrStatement();
    }
    @Override
    public void visit(SynchronizedStmt stmt, final ClassMethodReport report) {
        report.incrStatement();
    }
    @Override
    public void visit(ThrowStmt stmt, final ClassMethodReport report) {
        report.incrStatement();
    }
    @Override
    public void visit(TryStmt stmt, final ClassMethodReport report) {
        report.incrStatement();
    }
    @Override
    public void visit(CatchClause stmt, final ClassMethodReport report) {
        report.incrStatement();
    }
    @Override
    public void visit(LocalClassDeclarationStmt stmt, final ClassMethodReport report) {
        report.incrStatement();
    }
    @Override
    public void visit(YieldStmt stmt, final ClassMethodReport report) {
        report.incrStatement();
    }

}
