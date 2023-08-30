/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.bce;

import io.github.dddplus.ast.report.CallGraphReport;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.generic.*;

class MethodVisitor extends EmptyVisitor {
    private final JavaClass javaClass;
    private final MethodGen methodGen;
    private final CallGraphReport report;

    private final ConstantPoolGen constantPoolGen;

    private final String callerClass;
    private final String callerMethod;

    private static final String methodStaticInit = "<clinit>";
    private static final String methodConstructor = "<init>";

    MethodVisitor(JavaClass jc, MethodGen m, CallGraphReport r) {
        javaClass = jc;
        methodGen = m;
        report = r;
        constantPoolGen = methodGen.getConstantPool();

        callerClass = javaClass.getClassName();
        callerMethod = methodGen.getName();
    }

    void start() {
        if (callerIgnored()) {
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
            if (calleeClass.startsWith("java") || calleeMethod.equals(methodConstructor)
                    || calleeMethod.equals(methodStaticInit)) {
                continue;
            }

            report.register(callerClass, callerMethod, calleeClass, calleeMethod);
        }
    }

    private boolean callerIgnored() {
        if (javaClass.isEnum()) {
            return true;
        }
        if (callerMethod.equals(methodConstructor)) {
            return true;
        }
        if (methodGen.isAbstract() || methodGen.isNative()) {
            return true;
        }

        return false;
    }

}
