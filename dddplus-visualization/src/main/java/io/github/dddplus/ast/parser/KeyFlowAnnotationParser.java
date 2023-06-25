/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast.parser;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.google.common.collect.Lists;
import io.github.dddplus.ast.model.KeyFlowEntry;
import lombok.Getter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * {@link io.github.dddplus.dsl.KeyFlow}.
 */
@Getter
public class KeyFlowAnnotationParser {
    private final MethodDeclaration methodDeclaration;
    private final String className;
    private String methodName;
    private static final Set<String> ignoredArgument = new HashSet<>();

    public KeyFlowAnnotationParser(MethodDeclaration methodDeclaration, String className) {
        this.methodDeclaration = methodDeclaration;
        this.className = className;
        this.methodName = methodDeclaration.getNameAsString();

        ignoredArgument.add("String");
        ignoredArgument.add("Boolean");
        ignoredArgument.add("boolean");
        ignoredArgument.add("Integer");
        ignoredArgument.add("int");
        ignoredArgument.add("Date");
    }

    public KeyFlowEntry parse(AnnotationExpr keyFlow) {
        KeyFlowEntry result = new KeyFlowEntry(className, methodName,
                JavaParserUtil.javadocFirstLineOf(methodDeclaration));

        if (methodDeclaration.getParameters() != null) {
            Set<String> realArguments = new TreeSet<>();
            for (Parameter parameter : methodDeclaration.getParameters()) {
                if (!ignoredArgument.contains(parameter.getTypeAsString())) {
                    realArguments.add(parameter.getTypeAsString());
                }
            }
            result.setRealArguments(realArguments);
        }

        if (keyFlow instanceof MarkerAnnotationExpr) {
            // 标注时没有指定任何属性
            return result;
        }

        NormalAnnotationExpr normalAnnotationExpr = (NormalAnnotationExpr) keyFlow;
        for (MemberValuePair memberValuePair : normalAnnotationExpr.getPairs()) {
            switch (memberValuePair.getNameAsString()) {
                case "name":
                    result.setMethodName(AnnotationFieldParser.stringFieldValue(memberValuePair));
                    break;

                case "remark":
                    result.setRemark(AnnotationFieldParser.stringFieldValue(memberValuePair));
                    break;

                case "actor":
                    // Class[] actor，只是为了注解值是可选的，实际使用只会用1个
                    result.setActor(AnnotationFieldParser.stringFieldValue(memberValuePair));
                    break;

                case "args":
                    List<String> args = Lists.newArrayList(AnnotationFieldParser.arrayFieldValue(memberValuePair));
                    result.setArgs(args);
                    break;

                case "rules":
                    result.setRules(AnnotationFieldParser.arrayFieldValue(memberValuePair));
                    break;

                case "produceEvent":
                    result.setEvents(AnnotationFieldParser.arrayFieldValue(memberValuePair));
                    break;


                case "modes":
                    result.setModes(AnnotationFieldParser.arrayFieldValue(memberValuePair));
                    break;

                case "modeClass":
                    Set<String> tmp = result.getModes();
                    tmp.addAll(AnnotationFieldParser.arrayFieldValue(memberValuePair));
                    result.setModes(tmp);
                    break;
            }
        }

        return result;
    }
}
