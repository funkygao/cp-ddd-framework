package io.github.dddplus.ast.report;

import io.github.dddplus.bce.CallGraphConfig;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CallGraphReportTest {

    @Test
    void dotNode() {
        CallGraphConfig config = new CallGraphConfig();
        config.initialize();
        config.getStyle().setSimpleClassName(false);
        CallGraphReport.Record record = new CallGraphReport.Record("io.github.dddplus.Foo", config);
        record.addMethod("foo");
        record.addMethod("bar");
        assertEquals("io_github_dddplus_Foo", record.dotNode());
    }

}