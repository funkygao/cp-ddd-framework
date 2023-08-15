/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast.parser;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.google.common.collect.Lists;
import io.github.dddplus.ast.model.KeyFlowEntry;
import lombok.Getter;

import java.nio.file.Path;
import java.util.List;

/**
 * {@link io.github.dddplus.dsl.KeyFlow}.
 */
@Getter
public class KeyFlowAnnotationParser {
    private final MethodDeclaration methodDeclaration;
    private final String className;
    private String methodName;

    public KeyFlowAnnotationParser(MethodDeclaration methodDeclaration, String className) {
        this.methodDeclaration = methodDeclaration;
        this.className = className;
        this.methodName = methodDeclaration.getNameAsString();
    }

    public KeyFlowEntry parse(AnnotationExpr keyFlow) {
        KeyFlowEntry entry = new KeyFlowEntry(className, methodName,
                JavaParserUtil.javadocFirstLineOf(methodDeclaration));
        entry.setRealArguments(JavaParserUtil.extractMethodArguments(methodDeclaration));

        if (keyFlow instanceof MarkerAnnotationExpr) {
            // 标注时没有指定任何属性
            return entry;
        }

        NormalAnnotationExpr normalAnnotationExpr = (NormalAnnotationExpr) keyFlow;
        for (MemberValuePair memberValuePair : normalAnnotationExpr.getPairs()) {
            switch (memberValuePair.getNameAsString()) {
                case "name":
                    entry.setMethodName(AnnotationFieldParser.singleFieldValue(memberValuePair));
                    break;

                case "remark":
                    entry.setRemark(AnnotationFieldParser.singleFieldValue(memberValuePair));
                    break;

                case "usecase":
                    entry.setUsecase(true);
                    break;

                case "actor":
                    // Class[] actor，只是为了注解值是可选的，实际使用只会用1个
                    entry.setActor(AnnotationFieldParser.singleFieldValue(memberValuePair));
                    break;

                case "args":
                    List<String> args = Lists.newArrayList(AnnotationFieldParser.arrayFieldValue(memberValuePair));
                    entry.setArgs(args);
                    break;

                case "async":
                    entry.setAsync(true);
                    break;

                case "polymorphism":
                    entry.setPolymorphism(true);
                    break;

                case "produceEvent":
                    entry.setEvents(AnnotationFieldParser.arrayFieldValue(memberValuePair));
                    break;

                case "useRawArgs":
                    entry.setRealArguments(JavaParserUtil.extractMethodArguments(methodDeclaration));
                    entry.setUseRawArgs(true);
                    break;
            }
        }

        entry.setNonPublic(!methodDeclaration.isPublic());
        entry.setPosition(normalAnnotationExpr.getRange().get().begin);
        setUpAbsolutePath(entry);
        return entry;
    }

    private void setUpAbsolutePath(KeyFlowEntry entry) {
        ClassOrInterfaceDeclaration classDeclaration = methodDeclaration.findAncestor(ClassOrInterfaceDeclaration.class).orElse(null);
        if (classDeclaration == null) {
            return;
        }

        CompilationUnit compilationUnit = classDeclaration.findCompilationUnit().get();
        Path sourcePath = compilationUnit.getStorage().get().getPath();
        String absolutePath = sourcePath.toAbsolutePath().toString();
        entry.setAbsolutePath(absolutePath);
    }
}
