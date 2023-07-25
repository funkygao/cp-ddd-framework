/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast.parser;

import com.github.javaparser.ast.expr.*;

import java.util.Set;
import java.util.TreeSet;

class AnnotationFieldParser {

    static Set<String> arrayFieldValue(Expression expression) {
        Set<String> result = new TreeSet<>();
        if (expression instanceof ArrayInitializerExpr) {
            // multiple values
            ArrayInitializerExpr arrayInitializerExpr = (ArrayInitializerExpr) expression;
            for (Expression exp : arrayInitializerExpr.getValues()) {
                result.add(expressionValue(exp));
            }
        } else {
            // single value
            result.add(expressionValue(expression));
        }

        return result;
    }

    static Set<String> arrayFieldValue(MemberValuePair memberValuePair) {
        return arrayFieldValue(memberValuePair.getValue());
    }

    static String singleFieldValue(MemberValuePair memberValuePair) {
        Expression expression = memberValuePair.getValue();
        return expressionValue(expression);
    }

    private static String expressionValue(Expression expression) {
        if (expression instanceof ClassExpr) {
            ClassExpr expr = (ClassExpr) expression;
            return expr.getTypeAsString();
        } else if (expression instanceof StringLiteralExpr) {
            StringLiteralExpr expr = (StringLiteralExpr) expression;
            return expr.getValue();
        } else if (expression instanceof FieldAccessExpr) {
            FieldAccessExpr fieldAccessExpr = (FieldAccessExpr) expression;
            return fieldAccessExpr.getNameAsString();
        }

        return "Unknown";
    }

}
