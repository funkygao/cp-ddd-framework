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
import java.nio.file.FileSystems;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Data
@Slf4j
public class CallGraphConfig implements Serializable {
    private static final String methodStaticInit = "<clinit>";
    private static final String methodConstructor = "<init>";

    private Ignore ignore;
    private Accept accept;
    private Boolean simpleClassName = false;

    public static CallGraphConfig fromFile(String filename) throws FileNotFoundException {
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new FileReader(filename));
        CallGraphConfig config = gson.fromJson(reader, CallGraphConfig.class);
        config.ignore.initialize();
        return config;
    }

    public boolean useSimpleClassName() {
        return simpleClassName;
    }

    private boolean builtinIgnoreCaller(MethodVisitor m) {
        // caller method
        if (m.callerMethod.contains("$") || m.callerClass.contains("$")) {
            return true;
        }

        if (m.callerMethod.equals(methodConstructor)) {
            return true;
        }

        if (m.methodGen == null) {
            // 判断结束
            return false;
        }

        if (m.methodGen.isAbstract() || m.methodGen.isNative()) {
            return true;
        }

        return false;
    }

    boolean ignoreCaller(MethodVisitor m) {
        if (builtinIgnoreCaller(m)) {
            return true;
        }

        // caller package
        if (ignore.ignoreCallerPackage(m.callerPackage)) {
            return true;
        }

        if (ignore.ignoreCallerMethod(m.callerMethod)) {
            return true;
        }

        // caller class
        if (ignore.ignoreClass(m.callerClass)) {
            return true;
        }

        if (m.methodGen == null) {
            // 判断结束
            return false;
        }

        if (ignore.enumClazz && m.javaClass.isEnum()) {
            // ignore all enum
            return true;
        }

        return false;
    }

    private boolean builtinIgnoreInvokeInstruction(MethodVisitor m, InvokeInstruction instruction, CallGraphEntry callGraphEntry) {
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

        if (calleeMethod.equals(methodConstructor) || calleeMethod.equals(methodStaticInit)) {
            return true;
        }
        if (calleeMethod.contains("$") || calleeClass.contains("$")) {
            // lambda
            return true;
        }

        if (instruction == null) {
            return false;
        }

        // getter/setter
        int args = instruction.getArgumentTypes(m.constantPoolGen).length;
        if (calleeMethod.startsWith("get") && args == 0) {
            log.debug("getter ignored: {}.{} -> {}.{}", m.callerClass, m.callerMethod, calleeClass, calleeMethod);
            return true;
        }
        if (calleeMethod.startsWith("set") && args == 1) {
            log.debug("setter ignored: {}.{} -> {}.{}", m.callerClass, m.callerMethod, calleeClass, calleeMethod);
            return true;
        }

        return false;
    }

    boolean ignoreInvokeInstruction(MethodVisitor m, InvokeInstruction instruction, CallGraphEntry callGraphEntry) {
        if (builtinIgnoreInvokeInstruction(m, instruction, callGraphEntry)) {
            return true;
        }

        // callee package
        String calleePackage = callGraphEntry.getCalleeClazz().substring(0,
                callGraphEntry.getCalleeClazz().lastIndexOf("."));
        if (ignore.ignoreCalleePackage(calleePackage)) {
            return true;
        }
        if (!accept.acceptPackage(calleePackage)) {
            return true;
        }

        // callee class
        if (ignore.ignoreClass(callGraphEntry.getCalleeClazz())) {
            return true;
        }

        // callee method
        if (ignore.ignoreCalleeMethod(callGraphEntry.getCalleeMethod())) {
            return true;
        }

        return false;
    }

    @Data
    public static class Ignore implements Serializable {
        private List<String> classes = new ArrayList<>(); // caller and callee classes
        private List<String> callerPackages = new ArrayList<>();
        private List<String> callerMethods = new ArrayList<>();
        private List<String> calleePackages = new ArrayList<>();
        private List<String> calleeMethods = new ArrayList<>();
        private Boolean enumClazz = true;

        private transient List<PathMatcher> classPatterns;
        private transient List<PathMatcher> callerPackagePatterns;
        private transient List<PathMatcher> callerMethodPatterns;
        private transient List<PathMatcher> calleePackagePatterns;
        private transient List<PathMatcher> calleeMethodPatterns;

        void initialize() {
            classPatterns = new ArrayList<>(classes.size());
            for (String regex : classes) {
                if (!regex.contains("*")) {
                    regex = "*" + regex;
                }
                classPatterns.add(FileSystems.getDefault().getPathMatcher("glob:" + regex));
            }
            callerPackagePatterns = new ArrayList<>(callerPackages.size());
            for (String regex : callerPackages) {
                if (!regex.contains("*")) {
                    regex = "*" + regex + "*";
                }
                callerPackagePatterns.add(FileSystems.getDefault().getPathMatcher("glob:" + regex));
            }
            callerMethodPatterns = new ArrayList<>(callerMethods.size());
            for (String regex : callerMethods) {
                callerMethodPatterns.add(FileSystems.getDefault().getPathMatcher("glob:" + regex));
            }

            calleePackagePatterns = new ArrayList<>(calleePackages.size());
            for (String regex : calleePackages) {
                if (!regex.contains("*")) {
                    regex = "*" + regex + "*";
                }
                calleePackagePatterns.add(FileSystems.getDefault().getPathMatcher("glob:" + regex));
            }

            calleeMethodPatterns = new ArrayList<>(calleeMethods.size());
            for (String regex : calleeMethods) {
                calleeMethodPatterns.add(FileSystems.getDefault().getPathMatcher("glob:" + regex));
            }
        }

        boolean ignoreCallerPackage(String callerPackage) {
            return match(callerPackagePatterns, callerPackage);
        }

        boolean ignoreCalleePackage(String calleePackage) {
            return match(calleePackagePatterns, calleePackage);
        }

        boolean ignoreCalleeMethod(String calleeMethod) {
            return match(calleeMethodPatterns, calleeMethod);
        }

        boolean ignoreCallerMethod(String callerMethod) {
            return match(callerMethodPatterns, callerMethod);
        }

        boolean ignoreClass(String clazz) {
            return match(classPatterns, clazz);
        }

        private boolean match(List<PathMatcher> patterns, String s) {
            for (PathMatcher pattern : patterns) {
                if (pattern.matches(Paths.get(s))) {
                    return true;
                }
            }
            return false;
        }
    }

    @Data
    public static class Accept implements Serializable {
        private List<String> packagePrefixes = new ArrayList<>();

        boolean acceptPackage(String pkgName) {
            if (packagePrefixes.isEmpty()) {
                return true;
            }

            // any match
            for (String prefix : packagePrefixes) {
                if (pkgName.startsWith(prefix)) {
                    return true;
                }
            }

            return false;
        }
    }

}