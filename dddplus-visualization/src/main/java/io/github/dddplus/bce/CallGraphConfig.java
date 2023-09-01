/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.bce;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import io.github.dddplus.ast.model.CallGraphEntry;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.bcel.generic.InvokeInstruction;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Serializable;
import java.util.List;

@Data
@Slf4j
public class CallGraphConfig implements Serializable {
    private static final String methodStaticInit = "<clinit>";
    private static final String methodConstructor = "<init>";

    private Ignore ignore;
    private Include include;

    public static CallGraphConfig fromFile(String filename) throws FileNotFoundException {
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new FileReader(filename));
        return gson.fromJson(reader, CallGraphConfig.class);
    }

    boolean ignoreCaller(MethodVisitor m) {
        if (m.methodGen.isAbstract() || m.methodGen.isNative()) {
            return true;
        }

        if (ignore.enumClazz && m.javaClass.isEnum()) {
            // ignore all enum
            return true;
        }

        if (m.callerMethod.equals(methodConstructor)) {
            return true;
        }

        for (String suffix : ignore.classSuffix) {
            if (m.callerClass.endsWith(suffix)) {
                return true;
            }
        }

        for (String pkg : ignore.calleePackagesLike) {
            if (m.callerPackage.contains(pkg)) {
                return true;
            }
        }

        if (m.callerMethod.contains("$") || m.callerClass.contains("$")) {
            return true;
        }

        return false;
    }

    boolean ignoreInvokeInstruction(MethodVisitor m, InvokeInstruction instruction, CallGraphEntry callGraphEntry) {
        final String calleeMethod = callGraphEntry.getCalleeMethod();
        final String calleeClass = callGraphEntry.getCalleeClazz();

        if (m.callerClass.equals(calleeClass)) {
            // 自己调用自己
            return true;
        }

        if (!callGraphEntry.getCalleeClazz().contains(".")) {
            // 没有包名的类
            return true;
        }

        for (String s : ignore.calleeMethods) {
            if (calleeMethod.equals(s)) {
                return true;
            }
        }

        for (String c : ignore.classSuffix) {
            if (calleeClass.endsWith(c)) {
                return true;
            }
        }

        if (calleeMethod.equals(methodConstructor) || calleeMethod.equals(methodStaticInit)) {
            return true;
        }
        if (calleeMethod.contains("$") || calleeClass.contains("$")) {
            // lambda
            return true;
        }

        int args = instruction.getArgumentTypes(m.constantPoolGen).length;
        if (calleeMethod.startsWith("get") && args == 0) {
            log.debug("getter ignored: {}.{} -> {}.{}", m.callerClass, m.callerMethod, calleeClass, calleeMethod);
            return true;
        }
        if (calleeMethod.startsWith("set") && args == 1) {
            log.debug("setter ignored: {}.{} -> {}.{}", m.callerClass, m.callerMethod, calleeClass, calleeMethod);
            return true;
        }

        String calleePackage = callGraphEntry.getCalleeClazz().substring(0, callGraphEntry.getCalleeClazz().lastIndexOf("."));
        for (String pkg : include.packagePrefixes) {
            if (!calleePackage.startsWith(pkg)) {
                return true;
            }
        }

        for (String pkg : ignore.calleePackagesLike) {
            if (calleePackage.contains(pkg)) {
                return true;
            }
        }



        return false;
    }

    @Data
    public static class Ignore implements Serializable {
        private List<String> calleePackagesLike;
        private List<String> classSuffix;
        private List<String> calleeMethods;
        private Boolean enumClazz = true;
    }

    @Data
    public static class Include implements Serializable {
        private List<String> packagePrefixes;
    }

}
