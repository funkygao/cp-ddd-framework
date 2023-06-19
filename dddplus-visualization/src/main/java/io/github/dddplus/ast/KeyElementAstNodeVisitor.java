/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import io.github.dddplus.ast.model.KeyModelEntry;
import io.github.dddplus.ast.model.KeyPropertyEntry;
import io.github.dddplus.ast.parser.JavaParserUtil;
import io.github.dddplus.ast.parser.KeyElementAnnotationParser;
import io.github.dddplus.ast.report.KeyModelReport;
import io.github.dddplus.dsl.KeyElement;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class KeyElementAstNodeVisitor extends VoidVisitorAdapter<KeyModelReport> {
    private final Set<String> ignoredAnnotations;

    public KeyElementAstNodeVisitor() {
        this.ignoredAnnotations = new HashSet<>();
    }

    public KeyElementAstNodeVisitor(Set<String> ignoredAnnotations) {
        this.ignoredAnnotations = ignoredAnnotations;
    }

    @Override
    public void visit(final FieldDeclaration fieldDeclaration, final KeyModelReport report) {
        super.visit(fieldDeclaration, report);

        if (!fieldDeclaration.isAnnotationPresent(KeyElement.class)) {
            return;
        }

        if (fieldDeclaration.isAnnotationPresent(Deprecated.class)) {
            System.out.printf("WARN: @KeyElement used on Deprecated %s\n", fieldDeclaration.getVariable(0).getNameAsString());
            return;
        }

        for (String annotation : ignoredAnnotations) {
            if (fieldDeclaration.isAnnotationPresent(annotation)) {
                System.out.printf("WARN: @KeyElement used on %s %s\n", annotation, fieldDeclaration.getVariable(0).getNameAsString());
                return;
            }
        }

        ClassOrInterfaceDeclaration parentClass = JavaParserUtil.getClass(fieldDeclaration.getParentNode().get());
        if (parentClass == null) {
            return;
        }

        CompilationUnit cu = (CompilationUnit) parentClass.getParentNode().get();
        final String packageName = cu.getPackageDeclaration().get().getNameAsString();

        final String className = parentClass.getNameAsString();
        KeyModelEntry entry = report.getOrCreateKeyModelEntryForActor(className);
        entry.setPackageName(packageName);
        entry.setJavadoc(JavaParserUtil.javadocFirstLineOf(parentClass)); // TODO performance waste
        AnnotationExpr annotationExpr = fieldDeclaration.getAnnotationByClass(KeyElement.class).get();
        Map<KeyElement.Type, KeyPropertyEntry> properties = new KeyElementAnnotationParser(fieldDeclaration, className).parse(annotationExpr);
        for (KeyElement.Type type : properties.keySet()) {
            entry.addField(type, properties.get(type));
        }
    }
}
