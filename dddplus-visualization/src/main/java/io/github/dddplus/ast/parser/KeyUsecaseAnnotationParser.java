/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast.parser;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import io.github.dddplus.ast.model.KeyUsecaseEntry;
import lombok.Getter;

import java.util.ArrayList;

/**
 * {@link io.github.dddplus.dsl.KeyBehavior}
 */
@Getter
public class KeyUsecaseAnnotationParser {
    private final MethodDeclaration methodDeclaration;
    private final String className;
    private String methodName;

    public KeyUsecaseAnnotationParser(MethodDeclaration methodDeclaration, String className) {
        this.methodDeclaration = methodDeclaration;
        this.className = className;
        this.methodName = methodDeclaration.getNameAsString();
    }

    public KeyUsecaseEntry parse(AnnotationExpr keyUsecase) {
        KeyUsecaseEntry entry = new KeyUsecaseEntry(className, methodName);
        entry.setJavadoc(JavaParserUtil.javadocFirstLineOf(methodDeclaration));
        if (keyUsecase instanceof MarkerAnnotationExpr) {
            // 标注时没有指定任何属性
            return entry;
        }

        NormalAnnotationExpr normalAnnotationExpr = (NormalAnnotationExpr) keyUsecase;
        for (MemberValuePair memberValuePair : normalAnnotationExpr.getPairs()) {
            switch (memberValuePair.getNameAsString()) {
                case "name":
                    this.methodName = AnnotationFieldParser.singleFieldValue(memberValuePair);
                    entry.setMethodName(this.methodName);
                    break;

                case "remark":
                    entry.setRemark(AnnotationFieldParser.singleFieldValue(memberValuePair));
                    break;

                case "consumesKeyEvent":
                    entry.setKeyEvent(AnnotationFieldParser.singleFieldValue(memberValuePair));
                    break;

                case "in":
                    entry.setIn(new ArrayList<>(AnnotationFieldParser.arrayFieldValue(memberValuePair)));
                    break;

                case "out":
                    entry.setOut(new ArrayList<>(AnnotationFieldParser.arrayFieldValue(memberValuePair)));
                    break;
            }
        }

        return entry;
    }
}
