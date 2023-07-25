/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast.enforcer;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import io.github.dddplus.ast.DomainModelAnalyzer;
import io.github.dddplus.ast.FileWalker;
import io.github.dddplus.ext.IDomainExtension;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 确保{@link io.github.dddplus.ext.IDomainExtension}的每个方法返回值不能是int/boolean等非包装类型.
 *
 * <p>否则，如果扩展点无默认实现，运行时抛出NPE.</p>
 */
@Slf4j
public class ExtensionMethodSignatureEnforcer {
    private File[] dirs;

    public ExtensionMethodSignatureEnforcer scan(File... dirs) {
        this.dirs = dirs;
        return this;
    }

    public void enforce() throws IOException {
        List<CompilationUnit> compilationUnits = new ArrayList<>();
        for (File dir : dirs) {
            new FileWalker(new DomainModelAnalyzer.ActualFilter(null), (level, path, file) -> {
                compilationUnits.add(FileWalker.silentParse(file));
            }).walkFrom(dir);
        }

        for (CompilationUnit compilationUnit : compilationUnits) {
            List<TypeDeclaration<?>> clazzs = compilationUnit.getTypes();
            for (TypeDeclaration<?> clazz : clazzs) {
                if (!clazz.isClassOrInterfaceDeclaration()) {
                    continue;
                }

                ClassOrInterfaceDeclaration extInterface = (ClassOrInterfaceDeclaration) clazz;
                if (!extInterface.isInterface() || !isDomainExtension(extInterface)) {
                    continue;
                }

                for (MethodDeclaration methodDeclaration : extInterface.getMethods()) {
                    if (methodDeclaration.getType().isPrimitiveType()) {
                        throw new RuntimeException(
                                String.format("%s#%s returns primitive type, not allowed",
                                        extInterface.getNameAsString(),
                                        methodDeclaration.getNameAsString()));
                    }
                }
            }
        }
    }

    private boolean isDomainExtension(ClassOrInterfaceDeclaration classOrInterfaceDeclaration) {
        for (ClassOrInterfaceType parent : classOrInterfaceDeclaration.getExtendedTypes()) {
            if (parent.getNameAsString().equals(IDomainExtension.class.getSimpleName())) {
                return true;
            }
        }
        return false;
    }
}
