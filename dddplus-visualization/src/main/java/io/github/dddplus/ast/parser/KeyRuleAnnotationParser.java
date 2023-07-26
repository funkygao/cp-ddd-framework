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

    public KeyRuleAnnotationParser(MethodDeclaration methodDeclaration, String className) {
        this.methodDeclaration = methodDeclaration;
        this.className = className;
        this.methodName = methodDeclaration.getNameAsString();
    }

    public KeyRuleEntry parse(AnnotationExpr keyRule) {
        KeyRuleEntry entry = new KeyRuleEntry();
        entry.setClassName(this.className);
        entry.setMethodName(this.methodName);
        entry.setRealMethodName(this.methodName);
        entry.setJavadoc(JavaParserUtil.javadocFirstLineOf(methodDeclaration));

        if (keyRule instanceof MarkerAnnotationExpr) {
            return entry;
        }

        NormalAnnotationExpr normalAnnotationExpr = (NormalAnnotationExpr) keyRule;
        for (MemberValuePair memberValuePair : normalAnnotationExpr.getPairs()) {
            switch (memberValuePair.getNameAsString()) {
                case "name":
                    entry.setMethodName(AnnotationFieldParser.singleFieldValue(memberValuePair));
                    break;

                case "remark":
                    entry.setRemark(AnnotationFieldParser.singleFieldValue(memberValuePair));
                    break;

                case "actor":
                    // Class[] actor，只是为了注解值是可选的，实际使用只会用1个
                    entry.setActor(AnnotationFieldParser.singleFieldValue(memberValuePair));
                    break;

                case "refer":
                    entry.setRefer(AnnotationFieldParser.arrayFieldValue(memberValuePair));
                    break;
            }
        }

        return entry;
    }
}
