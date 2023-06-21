/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast.parser;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import io.github.dddplus.ast.model.KeyPropertyEntry;
import io.github.dddplus.dsl.KeyElement;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * {@link io.github.dddplus.dsl.KeyElement}
 */
@Getter
public class KeyElementAnnotationParser {
    private final FieldDeclaration fieldDeclaration;
    private final String className;

    public KeyElementAnnotationParser(FieldDeclaration fieldDeclaration, String className) {
        this.fieldDeclaration = fieldDeclaration;
        this.className = className;
    }

    public Map<KeyElement.Type, KeyPropertyEntry> parse(AnnotationExpr keyElement) {
        List<KeyElement.Type> types = new ArrayList<>();
        KeyPropertyEntry entry = new KeyPropertyEntry();
        entry.setJavadoc(JavaParserUtil.javadocFirstLineOf(fieldDeclaration));
        entry.setRealName(fieldDeclaration.getVariable(0).getNameAsString());
        entry.setName(entry.getRealName());
        entry.setClassName(this.className);
        NormalAnnotationExpr keyElementExpr = (NormalAnnotationExpr) keyElement;
        for (MemberValuePair memberValuePair : keyElementExpr.getPairs()) {
            switch (memberValuePair.getNameAsString()) {
                case "name":
                    entry.setName(AnnotationFieldParser.stringFieldValue(memberValuePair));
                    break;

                case "remark":
                    entry.setRemark(AnnotationFieldParser.stringFieldValue(memberValuePair));
                    break;

                case "types":
                    for (String typeStr : AnnotationFieldParser.arrayFieldValue(memberValuePair)) {
                        types.add(KeyElement.Type.valueOf(typeStr));
                    }
                    break;
            }
        }

        Map<KeyElement.Type, KeyPropertyEntry> result = new TreeMap<>();
        for (KeyElement.Type type : types) {
            result.put(type, entry);
        }

        return result;
    }

}
