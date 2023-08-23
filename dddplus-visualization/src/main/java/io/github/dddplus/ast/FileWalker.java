/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * 文件系统遍历器.
 *
 * <p>JavaParser一次只能分析一个java source file，通过{@link FileWalker}可以递归地遍历目录树.</p>
 * <pre>
 * {@code
 *
 * new FileWalker((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {
 *     new ClassMethodDistributionAstNodeVisitor().visit(FileWalker.silentParse(file), result);
 * }).walkFrom(domainModuleRoot());
 * }
 * </pre>
 */
public class FileWalker {
    public interface FileHandler {
        void handle(int level, String path, File file);
    }

    public interface Filter {
        boolean interested(int level, String path, File file);
    }

    private final FileHandler fileHandler;
    private final Filter filter;

    public FileWalker(Filter filter, FileHandler fileHandler) {
        this.filter = filter;
        this.fileHandler = fileHandler;
    }

    /**
     * Handy AST parsing based upon {@link StaticJavaParser#parse(File)}.
     *
     * <p>它忽略了文件不存在的异常，使用起来更方便</p>
     * <p>否则，就需要try catch</p>
     *
     * @param file java src file
     * @return AST root
     */
    public static CompilationUnit silentParse(final File file) {
        try {
            // JavaParser compiles java source code into AST
            // Additionally, it provides a convenience mechanism to navigate the tree
            return StaticJavaParser.parse(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (Throwable ignored) {
            throw new RuntimeException(file.getAbsolutePath(), ignored);
        }
    }

    /**
     * 从指定目录开始递归遍历.
     *
     * @param roots root dirs
     */
    public void walkFrom(File ...roots) {
        for (File root : roots) {
            walkFrom(0, "", root);
        }
    }

    private void walkFrom(int level, String path, File file) {
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                walkFrom(level + 1, path + "/" + child.getName(), child);
            }
        } else {
            if (filter.interested(level, path, file)) {
                fileHandler.handle(level, path, file);
            }
        }
    }
}

