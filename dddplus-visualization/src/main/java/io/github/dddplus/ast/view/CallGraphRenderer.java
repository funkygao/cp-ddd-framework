/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast.view;

import io.github.dddplus.ast.model.CallGraphEntry;
import io.github.dddplus.ast.parser.JavaParserUtil;
import io.github.dddplus.ast.report.CallGraphReport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * DSL -> Reverse Engineering Model -> method call -> dot DSL.
 */
public class CallGraphRenderer implements IRenderer {
    private CallGraphReport callGraphReport;
    private String targetCallGraphDotFile;

    private String splines = null;
    private StringBuilder content = new StringBuilder();

    public CallGraphRenderer targetCallGraphDotFile(String targetFile) {
        this.targetCallGraphDotFile = targetFile;
        return this;
    }

    /**
     * 控制edge的线条布局.
     *
     * @param splines ortho | spline | polyline | curved
     * @return
     */
    public CallGraphRenderer splines(String splines) {
        this.splines = splines;
        return this;
    }

    public CallGraphRenderer withReport(CallGraphReport report) {
        this.callGraphReport = report;
        return this;
    }

    public void render() throws IOException {
        if (targetCallGraphDotFile != null) {
            renderCallGraph();
        }
    }

    private void renderCallGraph() throws IOException {
        append("digraph G {")
                .append(NEWLINE)
                .setupSkin()
                .renderNodes()
                .append(NEWLINE)
                .renderEdges()
                .append("}");

        JavaParserUtil.dumpToFile(targetCallGraphDotFile, content.toString());
    }

    private CallGraphRenderer setupSkin() {
        append(TAB).append("labelloc = \"t\";").append(NEWLINE);
        append(TAB).append("rankdir=LR;").append(NEWLINE);
        if (splines != null) {
            append(TAB).append(String.format("splines = %s;", splines)).append(NEWLINE);
        }
        append(TAB).append("node [shape=record];").append(NEWLINE);
        append(TAB).append("edge [style = dashed, arrowsize=0.4, fontsize=6];").append(NEWLINE);
        append(NEWLINE);
        return this;
    }

    private CallGraphRenderer appendEscape(String s) {
        return append("\"").append(s).append("\"");
    }

    private CallGraphRenderer renderNodes() {
        for (CallGraphReport.Record calleeClazz : callGraphReport.calleeRecords()) {
            append(TAB).appendEscape(calleeClazz.dotNode())
                    .append(" [label=\"");
            List<String> list = new ArrayList<>();
            list.add(String.format("<%s> %s", calleeClazz.dotNode(), calleeClazz.dotNode()));
            for (String method : calleeClazz.getMethods()) {
                list.add(String.format("<%s> %s", method, method));
            }
            append(String.join("|", list));
            append("\"];").append(NEWLINE);
        }

        for (CallGraphReport.Record callerClazz : callGraphReport.callerRecords()) {
            append(TAB).appendEscape(callerClazz.dotNode())
                    .append(" [label=\"");
            List<String> list = new ArrayList<>();
            list.add(String.format("<%s> %s", callerClazz.dotNode(), callerClazz.dotNode()));
            for (String method : callerClazz.getMethods()) {
                list.add(String.format("<%s> %s", method, method));
            }
            append(String.join("|", list));
            append("\"];").append(NEWLINE);
        }

        return this;
    }

    private CallGraphRenderer renderEdges() {
        Set<String> edges = new HashSet<>();
        for (CallGraphEntry entry : callGraphReport.sortedEntries()) {
            // A的多个方法可能调用同一个callee：merge
            String key = entry.getCallerClazz() + ":" + entry.calleeNode();
            if (edges.contains(key)) {
                continue;
            }
            edges.add(key);

            String callerNode = entry.callerNode();
            append(TAB).append(callerNode).append(" -> ")
                    .append(entry.calleeNode());
            append(NEWLINE);

        }
        return this;
    }

    private CallGraphRenderer append(String s) {
        content.append(s);
        return this;
    }
}
