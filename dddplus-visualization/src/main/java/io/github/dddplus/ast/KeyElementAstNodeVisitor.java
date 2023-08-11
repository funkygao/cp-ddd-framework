/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast;

import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import io.github.dddplus.ast.model.KeyModelEntry;
import io.github.dddplus.ast.model.KeyPropertyEntry;
import io.github.dddplus.ast.model.KeyRelationEntry;
import io.github.dddplus.ast.parser.JavaParserUtil;
import io.github.dddplus.ast.parser.KeyElementAnnotationParser;
import io.github.dddplus.ast.report.KeyModelReport;
import io.github.dddplus.dsl.KeyElement;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

class KeyElementAstNodeVisitor extends VoidVisitorAdapter<KeyModelReport> {
    private final Set<String> ignoredAnnotations;
    private final boolean parseRawModel;

    public KeyElementAstNodeVisitor(boolean parseRawModel, Set<String> ignoredAnnotations) {
        this.parseRawModel = parseRawModel;
        this.ignoredAnnotations = ignoredAnnotations;
    }

    @Override
    public void visit(final EnumConstantDeclaration fieldDeclaration, final KeyModelReport report) {
        super.visit(fieldDeclaration, report);

        if (!fieldDeclaration.isAnnotationPresent(KeyElement.class)
                || fieldDeclaration.isAnnotationPresent(Deprecated.class)) {
            return;
        }

        TypeDeclaration typeDeclaration = JavaParserUtil.getTypeDeclaration(fieldDeclaration.getParentNode().get());
        if (typeDeclaration == null || !(typeDeclaration instanceof EnumDeclaration)) {
            // should never happen
            return;
        }

        EnumDeclaration parentClass = (EnumDeclaration) typeDeclaration;
        final String packageName = JavaParserUtil.packageName(parentClass);
        final String className = parentClass.getNameAsString();
        KeyModelEntry entry = report.getOrCreateKeyModelEntryForActor(className);
        entry.setEnumType(true);
        entry.setPackageName(packageName);
        if (!entry.hasJavadoc()) {
            entry.setJavadoc(JavaParserUtil.javadocFirstLineOf(parentClass));
        }
        AnnotationExpr annotationExpr = fieldDeclaration.getAnnotationByClass(KeyElement.class).get();
        KeyElementAnnotationParser parser = new KeyElementAnnotationParser(parentClass, fieldDeclaration, className);
        Map<KeyElement.Type, KeyPropertyEntry> properties = parser.parse(annotationExpr);
        for (KeyElement.Type type : properties.keySet()) {
            entry.addField(type, properties.get(type));
        }
    }

    @Override
    public void visit(final FieldDeclaration fieldDeclaration, final KeyModelReport report) {
        super.visit(fieldDeclaration, report);

        ClassOrInterfaceDeclaration parentClass = JavaParserUtil.getClass(fieldDeclaration.getParentNode().get());
        if (parentClass == null) {
            return;
        }

        if (parseRawModel) {
            if (!parentClass.isInterface()
                    && !parentClass.isAbstract()
                    && !fieldDeclaration.isStatic()) {
                parseRawModel(fieldDeclaration, report);
            }
        }

        if (!fieldDeclaration.isAnnotationPresent(KeyElement.class)) {
            return;
        }

        if (fieldDeclaration.isAnnotationPresent(Deprecated.class)) {
            System.out.printf("SKIP: @KeyElement used on Deprecated %s\n", fieldDeclaration.getVariable(0).getNameAsString());
            return;
        }

        for (String annotation : ignoredAnnotations) {
            if (fieldDeclaration.isAnnotationPresent(annotation)) {
                System.out.printf("SKIP: @KeyElement used on %s %s\n", annotation, fieldDeclaration.getVariable(0).getNameAsString());
                return;
            }
        }

        final String packageName = JavaParserUtil.packageName(parentClass);
        final String className = parentClass.getNameAsString();
        KeyModelEntry entry = report.getOrCreateKeyModelEntryForActor(className);
        entry.setPackageName(packageName);
        if (!entry.hasJavadoc()) {
            entry.setJavadoc(JavaParserUtil.javadocFirstLineOf(parentClass));
        }
        AnnotationExpr annotationExpr = fieldDeclaration.getAnnotationByClass(KeyElement.class).get();
        KeyElementAnnotationParser parser = new KeyElementAnnotationParser(parentClass, fieldDeclaration, className);
        Map<KeyElement.Type, KeyPropertyEntry> properties = parser.parse(annotationExpr);
        for (KeyElement.Type type : properties.keySet()) {
            entry.addField(type, properties.get(type));
        }

        Optional<KeyRelationEntry> relationEntry = parser.extractKeyRelation();
        if (relationEntry.isPresent()) {
            report.getModel().getKeyRelationReport().add(relationEntry.get());
        }
    }

    private void parseRawModel(final FieldDeclaration fieldDeclaration, final KeyModelReport report) {
        ClassOrInterfaceDeclaration parentClass = JavaParserUtil.getClass(fieldDeclaration.getParentNode().get());
        if (parentClass == null) {
            return;
        }

        if (fieldDeclaration.isStatic()
                || fieldDeclaration.isAnnotationPresent(Resource.class)) {
            return;
        }

        final String className = parentClass.getNameAsString();
        final String fieldName = fieldDeclaration.getVariable(0).getNameAsString();
        KeyModelEntry entry = report.getOrCreateRawModelEntry(className);
        entry.addRawField(fieldName);
    }
}
