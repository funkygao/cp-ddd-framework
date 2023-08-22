/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast.parser;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.*;
import com.google.common.collect.Lists;
import io.github.dddplus.ast.model.KeyBehaviorEntry;
import lombok.Getter;

import java.util.Set;
import java.util.TreeSet;

/**
 * {@link io.github.dddplus.dsl.KeyBehavior}
 */
@Getter
public class KeyBehaviorAnnotationParser {
    private final MethodDeclaration methodDeclaration;
    private final String packageName;
    private final String className;
    private String methodName;

    private Set<String> rules = new TreeSet<>();
    private Set<String> modes = new TreeSet<>();

    public KeyBehaviorAnnotationParser(MethodDeclaration methodDeclaration, String className, String packageName) {
        this.methodDeclaration = methodDeclaration;
        this.packageName = packageName;
        this.className = className;
        this.methodName = methodDeclaration.getNameAsString();
    }

    public KeyBehaviorEntry parse(AnnotationExpr keyBehavior) {
        KeyBehaviorEntry entry = new KeyBehaviorEntry(className, methodName);
        entry.setJavadoc(JavaParserUtil.javadocFirstLineOf(methodDeclaration));
        entry.setPackageName(packageName);

        if (keyBehavior instanceof MarkerAnnotationExpr) {
            // 标注时没有指定任何属性
            // @KeyBehavior
            return entry;
        }

        NormalAnnotationExpr normalAnnotationExpr = (NormalAnnotationExpr) keyBehavior;
        for (MemberValuePair memberValuePair : normalAnnotationExpr.getPairs()) {
            switch (memberValuePair.getNameAsString()) {
                case "name":
                    this.methodName = AnnotationFieldParser.singleFieldValue(memberValuePair);
                    entry.setMethodName(methodName);
                    break;

                case "remark":
                    entry.setRemark(AnnotationFieldParser.singleFieldValue(memberValuePair));
                    break;

                case "produceEvent":
                    entry.setEvents(AnnotationFieldParser.arrayFieldValue(memberValuePair));
                    break;

                case "async":
                    entry.setAsync(true);
                    break;

                case "args":
                    entry.setArgs(Lists.newArrayList(AnnotationFieldParser.arrayFieldValue(memberValuePair)));
                    break;

                case "useRawArgs":
                    entry.setRealArguments(JavaParserUtil.extractMethodArguments(methodDeclaration));
                    entry.setUseRawArgs(true);
                    break;

                case "abstracted":
                    entry.setAbstracted(true);
                    break;
            }
        }

        return entry;
    }
}
