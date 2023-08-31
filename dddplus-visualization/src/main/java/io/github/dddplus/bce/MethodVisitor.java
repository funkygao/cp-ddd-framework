/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.bce;

import io.github.dddplus.ast.model.CallGraphEntry;
import io.github.dddplus.ast.report.CallGraphReport;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.generic.*;

class MethodVisitor extends EmptyVisitor {
    final JavaClass javaClass;
    final MethodGen methodGen;
    final CallGraphReport report;
    final CallGraphConfig config;

    final ConstantPoolGen constantPoolGen;

    final String callerPackage;
    final String callerClass;
    final String callerMethod;

    MethodVisitor(JavaClass jc, MethodGen m, CallGraphReport r, CallGraphConfig c) {
        javaClass = jc;
        methodGen = m;
        report = r;
        config = c;
        constantPoolGen = methodGen.getConstantPool();

        // cache
        callerPackage = javaClass.getPackageName();
        callerClass = javaClass.getClassName();
        callerMethod = methodGen.getName();
    }

    void start() {
        if (config.ignoreCaller(this)) {
            return;
        }

        // 遍历该方法里的调用关系指令集
        for (InstructionHandle ih : methodGen.getInstructionList()) {
            Instruction instruction = ih.getInstruction();
            if (!(instruction instanceof InvokeInstruction)) {
                continue;
            }

            InvokeInstruction invokeInstruction = (InvokeInstruction) instruction;
            String calleeMethod = invokeInstruction.getMethodName(constantPoolGen);
            String calleeClass = invokeInstruction.getClassName(constantPoolGen);
            CallGraphEntry callGraphEntry = new CallGraphEntry(callerClass, callerMethod, calleeClass, calleeMethod);
            if (config.ignoreInvokeInstruction(this, invokeInstruction, callGraphEntry)) {
                continue;
            }

            report.register(callGraphEntry);
        }
    }

}
