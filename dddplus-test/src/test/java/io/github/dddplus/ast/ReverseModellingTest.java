package io.github.dddplus.ast;

import io.github.dddplus.ast.model.*;
import io.github.dddplus.ast.report.*;
import io.github.dddplus.dsl.KeyElement;
import io.github.design.CheckAdvancedRule;
import io.github.design.CheckTask;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReverseModellingTest {

    @Test
    void demoClassMethodDistributionAstNodeVisitor() throws IOException {
        ClassMethodReport result = new ClassMethodReport();
        File domainModuleRoot = moduleRoot("dddplus-test");
        new FileWalker((level, path, file) -> path.endsWith(".java") && (path.contains("domain") && !path.contains("fixme") || path.contains("flow")), (level, path, file) -> {
            new ClassMethodDistributionAstNodeVisitor().visit(FileWalker.silentParse(file), result);
        }).walkFrom(domainModuleRoot);

        ClassMethodReport.ClassInfo classInfo = result.getClassInfo();
        assertTrue(classInfo.getPublicClasses().size() > 9);
        ClassMethodReport.MethodInfo methodInfo = result.getMethodInfo();
        assertTrue(methodInfo.getPublicMethods().size() > 11);
    }

    @Test
    void demoKeyElementAstNodeVisitor() throws IOException {
        KeyModelReport report = new KeyModelReport();
        File domainModuleRoot = moduleRoot("dddplus-test");
        new FileWalker((level, path, file) -> !path.contains("/target/") && path.endsWith(".java") && path.contains("design"), (level, path, file) -> {
            new KeyElementAstNodeVisitor().visit(FileWalker.silentParse(file), report);
        }).walkFrom(domainModuleRoot);
        KeyModelEntry checkTaskElements = report.getOrCreateKeyModelEntryForActor(CheckTask.class.getSimpleName());
        assertTrue(checkTaskElements.getProperties().containsKey(KeyElement.Type.Lifecycle));
        List<KeyPropertyEntry> lifecycles = checkTaskElements.getProperties().get(KeyElement.Type.Lifecycle);
        boolean containsTheStatus = false;
        for (KeyPropertyEntry entry : lifecycles) {
            if (entry.getName().equals("theStatus")) {
                containsTheStatus = true;

                assertEquals("here we go for status.", entry.getJavadoc());
                break;
            }
        }
        assertTrue(containsTheStatus);
    }

    @Test
    void demoKeyBehaviorAstNodeVisitor() throws IOException {
        KeyBehaviorReport report = new KeyBehaviorReport();
        File domainModuleRoot = moduleRoot("dddplus-test");
        new FileWalker((level, path, file) -> !path.contains("/target/") && path.endsWith(".java") && path.contains("design"), (level, path, file) -> {
            new KeyBehaviorAstNodeVisitor().visit(FileWalker.silentParse(file), report);
        }).walkFrom(domainModuleRoot);
        KeyBehaviorEntry entry = report.actorKeyBehaviors("CheckTask").get(0);
        assertEquals("复核", entry.getMethodName());
        assertEquals("ok", entry.getRemark());
        assertEquals(2, entry.getRules().size());
        assertEquals(entry.getModes().size(), 2);
        assertTrue(entry.getRules().contains("CheckBasicRule"));
        assertTrue(entry.getRules().contains(CheckAdvancedRule.class.getSimpleName()));
    }

    @Test
    void demoKeyRuleAstNodeVisitor() throws IOException {
        KeyRuleReport report = new KeyRuleReport();
        File domainModuleRoot = moduleRoot("dddplus-test");
        new FileWalker((level, path, file) -> !path.contains("/target/") && path.endsWith(".java") && path.contains("design"), (level, path, file) -> {
            new KeyRuleAstNodeVisitor().visit(FileWalker.silentParse(file), report);
        }).walkFrom(domainModuleRoot);
        List<KeyRuleEntry> rules = report.getKeyRules().get(CheckTask.class.getSimpleName());
        assertEquals(1, rules.size());
        assertEquals(CheckTask.class.getSimpleName(), rules.get(0).getClassName());
        assertEquals("isDone", rules.get(0).getMethodName());
    }

    @Test
    void demoKeyFlowAstNodeVisitor() throws IOException {
        KeyFlowReport report = new KeyFlowReport();
        File domainModuleRoot = moduleRoot("dddplus-test");
        new FileWalker((level, path, file) -> !path.contains("/target/") && path.endsWith(".java") && path.contains("design"), (level, path, file) -> {
            new KeyFlowAstNodeVisitor().visit(FileWalker.silentParse(file), report);
        }).walkFrom(domainModuleRoot);
        Set<String> actors = report.actors();
        assertEquals(2, actors.size());
        String firstActor = actors.iterator().next();
        assertEquals("CheckTask", firstActor);
        KeyFlowEntry check = report.keyFlowEntriesOfActor(firstActor).get(0);
        assertEquals("CheckTaskDomainService", check.getClassName());
        assertEquals("CheckTask", check.getActor());
    }

    @Test
    void demoAggregateAsNodeVisitor() throws IOException {
        AggregateReport report = new AggregateReport();
        File domainModuleRoot = moduleRoot("dddplus-test");
        new FileWalker((level, path, file) -> !path.contains("/target/") && path.endsWith(".java") && path.contains("design"), (level, path, file) -> {
            new AggregateAstNodeVisitor().visit(FileWalker.silentParse(file), report);
        }).walkFrom(domainModuleRoot);
        assertTrue(report.size() > 0);
        assertEquals("io.github.design", report.get(0).getPackageName());
    }

    static File moduleRoot(String module) throws IOException {
        return (projectRoot().listFiles(f -> f.getName().equals(module)))[0];
    }

    private static File projectRoot() throws IOException {
        File currentDir = new ClassPathResource("").getFile(); // dddplus-test/target/test-classes
        return currentDir
                .getParentFile()
                .getParentFile()
                .getParentFile();
    }

}