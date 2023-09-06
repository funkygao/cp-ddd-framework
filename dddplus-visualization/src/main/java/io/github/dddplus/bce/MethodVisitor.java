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

    MethodVisitor(String callerPackage, String callerClass, String callerMethod, CallGraphConfig c) {
        this.callerPackage = callerPackage;
        this.callerClass = callerClass;
        this.callerMethod = callerMethod;
        config = c;

        javaClass = null;
        methodGen = null;
        report = null;
        constantPoolGen = null;
    }

    void start() {
        if (config.ignoreCaller(this)) {
            return;
        }

        if (methodGen.getInstructionList() == null) {
            // e,g. abstract method
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
            CallGraphEntry callGraphEntry = new CallGraphEntry(config, callerClass, callerMethod, calleeClass, calleeMethod);
            if (config.ignoreInvokeInstruction(this, invokeInstruction, callGraphEntry)) {
                continue;
            }

            if (invokeInstruction instanceof INVOKEINTERFACE) {
                //  The `visitINVOKEINTERFACE` instruction is used to invoke an interface method dynamically at runtime.
                //  It is similar to the `visitINVOKEVIRTUAL` instruction, but it is specifically designed to be used with methods of interfaces.
                callGraphEntry.setInvokeInterface(true);
            }
            if (invokeInstruction instanceof INVOKESTATIC) {
                callGraphEntry.setInvokeStatic(true);
            }

            report.register(callGraphEntry);
        }
    }

}
