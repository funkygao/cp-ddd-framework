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
import io.github.dddplus.ast.model.KeyRelationEntry;
import io.github.dddplus.dsl.KeyElement;
import io.github.dddplus.dsl.KeyRelation;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * {@link io.github.dddplus.dsl.KeyElement}
 */
@Getter
public class KeyElementAnnotationParser {
    private static Pattern pattern = Pattern.compile("(\\w+)<(\\w+)>");

    private final FieldDeclaration fieldDeclaration;
    private final String className;
    private KeyPropertyEntry propertyEntry;

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

                case "byType":
                    // we assume 'if byType is specified it is always true'
                    // will overwrite `name`
                    entry.setName(fieldDeclaration.getElementType().asString());
                    break;
            }
        }

        propertyEntry = entry;
        Map<KeyElement.Type, KeyPropertyEntry> result = new TreeMap<>();
        for (KeyElement.Type type : types) {
            result.put(type, entry);
        }

        return result;
    }

    public Optional<KeyRelationEntry> extractKeyRelation() {
        RelationToClazz typeToClazz = keyRelationTypeOf(fieldDeclaration.getElementType().asString());
        if (typeToClazz == null) {
            return Optional.empty();
        }

        // bingo!
        KeyRelationEntry entry = new KeyRelationEntry();
        entry.setJavadoc(propertyEntry.getJavadoc());
        entry.setRemark(propertyEntry.getRemark());
        entry.setLeftClass(propertyEntry.getClassName());
        entry.setRightClass(typeToClazz.rightClass);
        entry.setType(KeyRelation.Type.valueOf(typeToClazz.relationType));
        return Optional.of(entry);
    }

    RelationToClazz keyRelationTypeOf(String elementType) {
        Matcher matcher = pattern.matcher(elementType);
        if (!matcher.find()) {
            return null;
        }

        String relationType = matcher.group(1);
        if (!KeyRelation.Type.match(relationType)) {
            return null;
        }

        return new RelationToClazz(relationType, matcher.group(2));
    }

    @AllArgsConstructor
    @Getter
    static class RelationToClazz {
        private String relationType;
        private String rightClass;
    }

}
