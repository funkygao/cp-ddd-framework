/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import io.github.dddplus.ast.parser.JavaParserUtil;
import io.github.dddplus.ast.report.ClassHierarchyReport;

class ClassHierarchyAstNodeVisitor extends VoidVisitorAdapter<ClassHierarchyReport> {

    @Override
    public void visit(final ClassOrInterfaceDeclaration classOrInterfaceDeclaration, final ClassHierarchyReport report) {
        super.visit(classOrInterfaceDeclaration, report);

        for (ClassOrInterfaceType parentClazz : classOrInterfaceDeclaration.getExtendedTypes()) {
            if (report.ignoreParentClass(parentClazz.getNameAsString())) {
                continue;
            }

            report.registerExtendsRelation(classOrInterfaceDeclaration.getNameAsString(), JavaParserUtil.javadocFirstLineOf(classOrInterfaceDeclaration), parentClazz.getNameAsString());
        }

        for (ClassOrInterfaceType parentClazz : classOrInterfaceDeclaration.getImplementedTypes()) {
            if (report.ignoreParentClass(parentClazz.getNameAsString())) {
                continue;
            }

            report.registerImplementsRelation(classOrInterfaceDeclaration.getNameAsString(), JavaParserUtil.javadocFirstLineOf(classOrInterfaceDeclaration), parentClazz.getNameAsString());
        }
    }

}
