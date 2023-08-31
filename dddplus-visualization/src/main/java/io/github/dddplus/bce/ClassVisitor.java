/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.bce;

import io.github.dddplus.ast.report.CallGraphReport;
import org.apache.bcel.classfile.EmptyVisitor;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.MethodGen;

class ClassVisitor extends EmptyVisitor {
    private final JavaClass javaClass;
    private final CallGraphReport report;
    private final CallGraphConfig config;

    private final ConstantPoolGen constantPool;

    ClassVisitor(JavaClass jc, CallGraphReport r, CallGraphConfig c) {
        javaClass = jc;
        report = r;
        config = c;
        constantPool = new ConstantPoolGen(javaClass.getConstantPool());
    }

    @Override
    public void visitJavaClass(JavaClass jc) {
        jc.getConstantPool().accept(this);

        for (Method method : jc.getMethods()) {
            method.accept(this);
        }
    }

    @Override
    public void visitMethod(Method method) {
        MethodGen mg = new MethodGen(method, javaClass.getClassName(), constantPool);
        new MethodVisitor(javaClass, mg, report, config)
                .start();
    }

    void start() {
        visitJavaClass(javaClass);
    }
}
