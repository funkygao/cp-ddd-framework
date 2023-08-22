/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast.parser;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.ClassExpr;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import io.github.dddplus.ast.model.KeyRelationEntry;
import lombok.Getter;

/**
 * {@link io.github.dddplus.dsl.KeyRelation}
 */
@Getter
public class KeyRelationAnnotationParser {
    private final ClassOrInterfaceDeclaration leftClassDeclaration;

    public KeyRelationAnnotationParser(ClassOrInterfaceDeclaration classDeclaration) {
        this.leftClassDeclaration = classDeclaration;
    }

    public KeyRelationEntry parse(AnnotationExpr keyRelation) {
        KeyRelationEntry entry = new KeyRelationEntry();
        entry.setJavadoc(JavaParserUtil.javadocFirstLineOf(leftClassDeclaration));
        entry.setLeftClass(leftClassDeclaration.getNameAsString());
        entry.setLeftClassPackageName(JavaParserUtil.packageName(leftClassDeclaration));

        NormalAnnotationExpr normalAnnotationExpr = (NormalAnnotationExpr) keyRelation;
        for (MemberValuePair memberValuePair : normalAnnotationExpr.getPairs()) {
            switch (memberValuePair.getNameAsString()) {
                case "type":
                    entry.setTypeInString(AnnotationFieldParser.singleFieldValue(memberValuePair));
                    break;

                case "remark":
                    entry.setRemark(AnnotationFieldParser.singleFieldValue(memberValuePair));
                    break;

                case "contextual":
                    entry.setContextual(true);
                    break;

                case "whom":
                    /*
                    public class Order {
                        public interface OrderLines extends HasMany<OrderLine> {
                            @KeyBehavior
                            int totalPrice();
                        }
                        @KeyRelation(whom = Order.OrderLines.class, type = Associate)
                        private OrderLines orderLines;
                    }

                    这需要处理一下，因为 KeyBehavior 注册时，entry.className 是 OrderLines，而不是 Order.OrderLines
                     */
                    ClassExpr classExpr = (ClassExpr) memberValuePair.getValue();
                    String rightClazz = classExpr.getTypeAsString();
                    if (rightClazz.contains(".")) {
                        rightClazz = rightClazz.substring(rightClazz.lastIndexOf('.') + 1);
                    }
                    entry.setRightClass(rightClazz);
                    entry.setRightClassPackageName(JavaParserUtil.packageOfKeyRelationRightClass(keyRelation, classExpr));
                    if (entry.getRightClassPackageName().isEmpty()) {
                        // 通过import没有找到right class，说明它们 same package
                        entry.setRightClassPackageName(entry.getLeftClassPackageName());
                    }
                    break;
            }
        }

        return entry;
    }
}
