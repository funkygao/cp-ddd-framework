/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast.parser;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import io.github.dddplus.ast.model.KeyEventEntry;
import io.github.dddplus.dsl.KeyEvent;
import lombok.Getter;

/**
 * {@link io.github.dddplus.dsl.KeyEvent}
 */
@Getter
public class KeyEventAnnotationParser {
    private final ClassOrInterfaceDeclaration classDeclaration;

    public KeyEventAnnotationParser(ClassOrInterfaceDeclaration classDeclaration) {
        this.classDeclaration = classDeclaration;
    }

    public KeyEventEntry parse(AnnotationExpr keyRelation) {
        KeyEventEntry result = new KeyEventEntry();
        result.setClassName(classDeclaration.getNameAsString());
        result.setJavadoc(JavaParserUtil.javadocFirstLineOf(classDeclaration));

        NormalAnnotationExpr normalAnnotationExpr = (NormalAnnotationExpr) keyRelation;
        for (MemberValuePair memberValuePair : normalAnnotationExpr.getPairs()) {
            switch (memberValuePair.getNameAsString()) {
                case "type":
                    result.setType(KeyEvent.Type.valueOf(AnnotationFieldParser.singleFieldValue(memberValuePair)));
                    break;

                case "remark":
                    result.setRemark(AnnotationFieldParser.singleFieldValue(memberValuePair));
                    break;
            }
        }

        return result;
    }
}
