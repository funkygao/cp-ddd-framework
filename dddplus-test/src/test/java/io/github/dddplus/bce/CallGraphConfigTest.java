package io.github.dddplus.bce;

import io.github.dddplus.ast.model.CallGraphEntry;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class CallGraphConfigTest {

    @Test
    void fromFile() throws FileNotFoundException {
        try {
            CallGraphConfig.fromJsonFile("../doc/none");
            fail();
        } catch (FileNotFoundException expected) {
        }

        CallGraphConfig config = CallGraphConfig.fromJsonFile("../doc/callgraph.json");
        assertTrue(config.getIgnore().isEnumClazz());
        assertNotNull(config.getIgnore().getCalleeMethods());
        assertTrue(config.useSimpleClassName());
    }

    @Test
    void toJsonString() throws FileNotFoundException {
        CallGraphConfig config = CallGraphConfig.fromJsonFile("../doc/callgraph.json");
        String json = config.jsonString();
        assertEquals("{\"ignore\":{\"classes\":[\"Dao\",\"JsonUtil\",\"CommonUtil\",\"InternalAopUtils\",\"BootstrapException\",\"StepsExecTemplate\",\"StepDef\",\"InterceptorDef\",\"BaseDecideStepsRouter\",\"DomainDef\"],\"callerPackages\":[\"*jdbc\"],\"callerMethods\":[\"equals\"],\"calleePackages\":[\"*jdbc.impl*\",\".enums\",\".dto\",\".dao\",\".repo\",\".mapper\"],\"calleeMethods\":[\"toString\",\"equals\",\"builder\",\"hashCode\",\"clone\"],\"enumClazz\":true,\"classInnerCall\":false,\"orphanNodes\":true},\"accept\":{\"packagePrefixes\":[\"io.git\"]},\"style\":{\"simpleClassName\":true,\"nodesep\":0.2,\"useCaseLayerClasses\":[\"*AppService*\"],\"aclClasses\":[\"*stock.*\"],\"externalPackages\":[]},\"relations\":[\"a -> b\"]}", config.jsonString());
        assertFalse(json.contains("\n"));
    }

    @Test
    void ignoreCaller() throws FileNotFoundException {
        CallGraphConfig config = CallGraphConfig.fromJsonFile("../doc/callgraph.json");
        MethodVisitor m = new MethodVisitor("io.git.dddplus.jdbc", "io.git.dddplus.jdbc.FooDao", "query", config);
        CallGraphEntry entry = new CallGraphEntry(config, "io.git.dddplus.jdbc.FooDao", "query", "io.git.dddplus.jdbc.impl.Bar", "bar");
        assertTrue(config.ignoreCaller(m));
        assertTrue(config.ignoreInvokeInstruction(m, null, entry));
        assertTrue(config.isUseCaseLayerClass("org.git.dddplus.FooAppServiceImpl"));
        assertFalse(config.isUseCaseLayerClass("FooListener"));
        config.getStyle().setUseCaseLayerClasses(Arrays.asList("*picking*AppService*"));
        config.initialize();
        assertTrue(config.isUseCaseLayerClass("com.foo.picking.application.task.PickingTaskAppServiceImpl"));
    }

    @Test
    void edgeInfo() {
        CallGraphConfig.Edge edge = new CallGraphConfig.Edge("a    ->   b   ", true);
        assertEquals("a", edge.caller());
        edge = new CallGraphConfig.Edge(" a.b.c.Foo    ->   Bar   ", true);
        assertEquals("Foo", edge.caller());
        assertEquals("Bar", edge.callee());

        try {
            new CallGraphConfig.Edge("a => b   ", true);
            fail();
        } catch (IllegalArgumentException expected) {
        }
        try {
            new CallGraphConfig.Edge("a -> b -> c  ", true);
            fail();
        } catch (IllegalArgumentException expected) {
        }

    }

}