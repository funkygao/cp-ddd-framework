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
import io.github.dddplus.ast.model.KeyRuleEntry;
import lombok.Getter;

/**
 * {@link io.github.dddplus.dsl.KeyRule}
 */
@Getter
public class KeyRuleAnnotationParser {
    private final MethodDeclaration methodDeclaration;
    private final String className;
    private String methodName;
    private String remark;

    public KeyRuleAnnotationParser(MethodDeclaration methodDeclaration, String className) {
        this.methodDeclaration = methodDeclaration;
        this.className = className;
        this.methodName = methodDeclaration.getNameAsString();
    }

    public KeyRuleEntry parse(AnnotationExpr keyFlow) {
        KeyRuleEntry result = new KeyRuleEntry();
        result.setClassName(this.className);
        result.setMethodName(this.methodName);
        result.setRealMethodName(this.methodName);
        result.setJavadoc(JavaParserUtil.javadocFirstLineOf(methodDeclaration));

        if (keyFlow instanceof MarkerAnnotationExpr) {
            return result;
        }

        NormalAnnotationExpr normalAnnotationExpr = (NormalAnnotationExpr) keyFlow;
        for (MemberValuePair memberValuePair : normalAnnotationExpr.getPairs()) {
            switch (memberValuePair.getNameAsString()) {
                case "name":
                    result.setMethodName(AnnotationFieldParser.stringFieldValue(memberValuePair));
                    break;

                case "remark":
                    result.setRemark(AnnotationFieldParser.stringFieldValue(memberValuePair));
                    break;

                case "refer":
                    result.setRefer(AnnotationFieldParser.arrayFieldValue(memberValuePair));
                    break;
            }
        }

        return result;
    }
}
