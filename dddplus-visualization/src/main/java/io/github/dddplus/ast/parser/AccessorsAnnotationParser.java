package io.github.dddplus.ast.parser;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import io.github.dddplus.ast.model.AccessorsEntry;
import lombok.AllArgsConstructor;

import java.util.Set;

@AllArgsConstructor
public class AccessorsAnnotationParser {
    private final ClassOrInterfaceDeclaration classOrInterfaceDeclaration;
    private final MethodDeclaration methodDeclaration;

    public AccessorsEntry parse(AnnotationExpr expr) {
        SingleMemberAnnotationExpr singleMemberAnnotationExpr = (SingleMemberAnnotationExpr) expr;
        Set<String> accessorClasses = AnnotationFieldParser.arrayFieldValue(singleMemberAnnotationExpr.getMemberValue());
        AccessorsEntry entry = new AccessorsEntry();
        entry.setAccessorsClasses(accessorClasses);
        entry.setClassName(classOrInterfaceDeclaration.getNameAsString());
        entry.setMethodName(methodDeclaration.getNameAsString());
        return entry;
    }

}
