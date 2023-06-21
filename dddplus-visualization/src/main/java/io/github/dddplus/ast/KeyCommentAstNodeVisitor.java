/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast;

import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class KeyCommentAstNodeVisitor extends VoidVisitorAdapter<Void> {
    
    @Override
    public void visit(final BlockComment comment, final Void report) {
        super.visit(comment, report);
    }

    @Override
    public void visit(final JavadocComment comment, final Void report) {
        super.visit(comment, report);
    }

    @Override
    public void visit(final LineComment comment, final Void report) {
        super.visit(comment, report);
    }

}
