/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast;

import io.github.dddplus.model.encapsulation.AllowedAccessors;

import java.io.File;

/**
 * 确保代码符合{@link AllowedAccessors}规范.
 */
public class AllowedAccessorsEnforcer {
    private File[] dirs;

    public AllowedAccessorsEnforcer scan(File... dirs) {
        this.dirs = dirs;
        return this;
    }

    public void enforce(FileWalker.Filter filter) {
        FileWalker.Filter actualFilter = new DomainModelAnalyzer.ActualFilter(filter);
        for (File dir : dirs) {
            AccessorsAstNodeVisitor accessorsAstNodeVisitor = new AccessorsAstNodeVisitor();

            // 1. parse annotated methods
            accessorsAstNodeVisitor.parseMethodDeclaration = true;
            new FileWalker(actualFilter, (level, path, file) -> {
                accessorsAstNodeVisitor.visit(FileWalker.silentParse(file), null);
            }).walkFrom(dir);

            // 2. parse method calls
            accessorsAstNodeVisitor.parseMethodDeclaration = false;
            new FileWalker(actualFilter, (level, path, file) -> {
                accessorsAstNodeVisitor.visit(FileWalker.silentParse(file), null);
            }).walkFrom(dir);
        }
    }
}
