/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import io.github.dddplus.ast.parser.JavaParserUtil;
import io.github.dddplus.ast.report.ClassHierarchyReport;

import java.util.ArrayList;
import java.util.List;

class ClassHierarchyAstNodeVisitor extends VoidVisitorAdapter<ClassHierarchyReport> {

    @Override
    public void visit(final ClassOrInterfaceDeclaration classOrInterfaceDeclaration, final ClassHierarchyReport report) {
        super.visit(classOrInterfaceDeclaration, report);

        // extends
        for (ClassOrInterfaceType parentClazz : classOrInterfaceDeclaration.getExtendedTypes()) {
            report.registerExtendsRelation(classOrInterfaceDeclaration.getNameAsString(),
                    JavaParserUtil.javadocFirstLineOf(classOrInterfaceDeclaration),
                    specifiedGenericTypes(parentClazz),
                    parentClazz.getNameAsString());
        }

        // implements
        for (ClassOrInterfaceType parentClazz : classOrInterfaceDeclaration.getImplementedTypes()) {
            report.registerImplementsRelation(classOrInterfaceDeclaration.getNameAsString(),
                    JavaParserUtil.javadocFirstLineOf(classOrInterfaceDeclaration),
                    specifiedGenericTypes(parentClazz),
                    parentClazz.getNameAsString());
        }
    }

    private List<String> specifiedGenericTypes(ClassOrInterfaceType parentClazz) {
        List<String> result = new ArrayList<>();
        NodeList<Type> genericTypes = parentClazz.getTypeArguments().orElse(null);
        if (genericTypes != null && genericTypes.isNonEmpty()) {
            for (Type type : genericTypes) {
                if (type instanceof ClassOrInterfaceType) {
                    ClassOrInterfaceType genericType = (ClassOrInterfaceType) type;
                    result.add(genericType.getNameAsString());
                }
            }
        }
        return result;
    }

}
