package io.github.dddplus.ast.parser;

import com.github.javaparser.ast.expr.*;

import java.util.Set;
import java.util.TreeSet;

class AnnotationFieldParser {

    static Set<String> arrayFieldValue(MemberValuePair memberValuePair) {
        Set<String> result = new TreeSet<>();
        if (memberValuePair.getValue() instanceof ArrayInitializerExpr) {
            // multiple values
            ArrayInitializerExpr arrayInitializerExpr = (ArrayInitializerExpr) memberValuePair.getValue();
            for (Expression expression : arrayInitializerExpr.getValues()) {
                result.add(expressionValue(expression));
            }
        } else {
            // single value
            result.add(singleFieldValue(memberValuePair));
        }

        return result;
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
