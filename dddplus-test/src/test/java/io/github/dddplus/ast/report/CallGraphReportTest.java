package io.github.dddplus.ast.report;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CallGraphReportTest {

    @Test
    void dotNode() {
        CallGraphReport.Record record = new CallGraphReport.Record("io.github.dddplus.Foo");
        record.addMethod("foo");
        record.addMethod("bar");
        assertEquals("io_github_dddplus_Foo", record.dotNode());
    }

}