/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast.parser;

import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.EnumConstantDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import io.github.dddplus.ast.model.KeyPropertyEntry;
import io.github.dddplus.ast.model.KeyRelationEntry;
import io.github.dddplus.dsl.KeyElement;
import io.github.dddplus.dsl.KeyRelation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * {@link io.github.dddplus.dsl.KeyElement}
 */
@Getter
@Slf4j
public class KeyElementAnnotationParser {
    private static Pattern pattern = Pattern.compile("(\\w+)<(\\w+)>");

    private final TypeDeclaration typeDeclaration;
    private final BodyDeclaration bodyDeclaration;
    private FieldDeclaration fieldDeclaration;
    private EnumConstantDeclaration enumConstantDeclaration;
    private final String className;
    private KeyPropertyEntry propertyEntry;

    public KeyElementAnnotationParser(TypeDeclaration classOrInterfaceDeclaration, BodyDeclaration bodyDeclaration, String className) {
        this.typeDeclaration = classOrInterfaceDeclaration;
        this.bodyDeclaration = bodyDeclaration;
        if (bodyDeclaration instanceof FieldDeclaration) {
            this.fieldDeclaration = (FieldDeclaration) bodyDeclaration;
        }
        if (bodyDeclaration instanceof EnumConstantDeclaration) {
            enumConstantDeclaration = (EnumConstantDeclaration) bodyDeclaration;
        }
        this.className = className;
    }

    public Map<KeyElement.Type, KeyPropertyEntry> parse(AnnotationExpr keyElement) {
        List<KeyElement.Type> types = new ArrayList<>();
        KeyPropertyEntry entry = new KeyPropertyEntry();
        entry.setJavadoc(JavaParserUtil.javadocFirstLineOf(bodyDeclaration));
        if (fieldDeclaration != null) {
            entry.setRealName(fieldDeclaration.getVariable(0).getNameAsString());
        } else {
            entry.setRealName(enumConstantDeclaration.getNameAsString());
        }
        entry.setName(entry.getRealName());
        entry.setClassName(this.className);
        if (keyElement instanceof MarkerAnnotationExpr) {
            types.add(KeyElement.Type.Structural);
        } else {
            NormalAnnotationExpr keyElementExpr = (NormalAnnotationExpr) keyElement;
            for (MemberValuePair memberValuePair : keyElementExpr.getPairs()) {
                switch (memberValuePair.getNameAsString()) {
                    case "name":
                        entry.setName(AnnotationFieldParser.singleFieldValue(memberValuePair));
                        break;

                    case "byType":
                        // we assume 'if byType is specified it is always true'
                        // will overwrite `name`
                        if (fieldDeclaration != null) {
                            entry.setName(fieldDeclaration.getElementType().asString());
                        }
                        break;

                    case "byJavadoc":
                        if (entry.getJavadoc() == null || entry.getJavadoc().isEmpty()) {
                            log.warn("empty javadoc on {}", entry.toString());
                        } else {
                            entry.setName(entry.getJavadoc());
                        }
                        break;

                    case "remark":
                        entry.setRemark(AnnotationFieldParser.singleFieldValue(memberValuePair));
                        break;

                    case "remarkFromJavadoc":
                        if (entry.getJavadoc() == null || entry.getJavadoc().isEmpty()) {
                            log.warn("empty javadoc on {}", entry.toString());
                        } else {
                            entry.setRemark(entry.getJavadoc());
                        }
                        break;

                    case "types":
                        for (String typeStr : AnnotationFieldParser.arrayFieldValue(memberValuePair)) {
                            types.add(KeyElement.Type.valueOf(typeStr));
                        }
                        break;
                }
            }

            if (types.isEmpty()) {
                // @KeyElement(byJavadoc = true)
                types.add(KeyElement.Type.Structural);
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
        if (fieldDeclaration == null) {
            return Optional.empty();
        }

        RelationToClazz typeToClazz = keyRelationTypeOf(fieldDeclaration.getElementType().asString());
        if (typeToClazz == null) {
            return Optional.empty();
        }

        // bingo!
        KeyRelationEntry entry = new KeyRelationEntry();
        entry.setJavadoc(propertyEntry.getJavadoc());
        entry.setRemark(propertyEntry.getRemark());
        entry.setLeftClassPackageName(JavaParserUtil.packageName(typeDeclaration));
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
