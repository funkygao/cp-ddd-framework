/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast.view;

import io.github.dddplus.ast.model.CallGraphEntry;
import io.github.dddplus.ast.model.PackageCrossRefEntry;
import io.github.dddplus.ast.model.ReverseEngineeringModel;
import io.github.dddplus.ast.report.CallGraphReport;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * DSL -> Reverse Engineering Model -> method call -> dot DSL.
 */
public class CallGraphRenderer implements IModelRenderer<CallGraphRenderer> {
    private CallGraphReport callGraphReport;
    private String targetCallGraphDotFile;
    private String targetPackageCrossRefDotFile;
    private boolean edgeShowsCallerMethod = false;

    private String splines = null;
    private StringBuilder content = new StringBuilder();

    public CallGraphRenderer edgeShowsCallerMethod() {
        this.edgeShowsCallerMethod = true;
        return this;
    }

    public CallGraphRenderer targetCallGraphDotFile(String targetFile) {
        this.targetCallGraphDotFile = targetFile;
        return this;
    }

    public CallGraphRenderer targetPackageCrossRefDotFile(String targetFile) {
        this.targetPackageCrossRefDotFile = targetFile;
        return this;
    }

    /**
     * 控制edge的线条布局.
     *
     * @param splines ortho | spline | polyline
     * @return
     */
    public CallGraphRenderer splines(String splines) {
        this.splines = splines;
        return this;
    }

    @Override
    public CallGraphRenderer withModel(ReverseEngineeringModel model) {
        this.callGraphReport = model.getCallGraphReport();
        return this;
    }

    @Override
    public void render() throws IOException {
        if (targetCallGraphDotFile != null) {
            renderCallGraph();
        }

        if (targetPackageCrossRefDotFile != null) {
            content.setLength(0);
            renderPackageCrossRef();
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

        File file = new File(targetCallGraphDotFile);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.append(content);
        }
    }

    private void renderPackageCrossRef() throws IOException {
        append("digraph G {")
                .append(NEWLINE)
                .setupSkin();
        for (PackageCrossRefEntry entry : callGraphReport.getPackageCrossRefEntries()) {
            append(TAB)
                    .append("\"").append(entry.getCallerPackage()).append("\"")
                    .append(" -> ")
                    .append("\"").append(entry.getCalleePackage()).append("\"")
                    .append(NEWLINE);
        }
        append("}");

        File file = new File(targetPackageCrossRefDotFile);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.append(content);
        }

    }

    private CallGraphRenderer setupSkin() {
        append(TAB).append("labelloc = \"t\";").append(NEWLINE);
        append(TAB).append("rankdir=LR;").append(NEWLINE);
        if (splines != null) {
            append(TAB).append(String.format("splines = %s;", splines)).append(NEWLINE);
        }
        append(TAB).append("node [shape=record];").append(NEWLINE);
        append(TAB).append("edge [style = dashed, fontsize=10];").append(NEWLINE);
        append(NEWLINE);
        return this;
    }

    private CallGraphRenderer renderNodes() {
        for (CallGraphReport.Record calleeClazz : callGraphReport.calleeRecords()) {
            append(TAB).append(calleeClazz.getClazz())
                    .append(" [label=\"");
            List<String> list = new ArrayList<>();
            list.add(String.format("<%s> %s", calleeClazz.getClazz(), calleeClazz.getClazz()));
            for (String method : calleeClazz.getMethods()) {
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
            if (!edgeShowsCallerMethod) {
                // A的多个方法可能调用同一个callee：merge
                String key = entry.getCallerClazz() + ":" + entry.calleeNode();
                if (edges.contains(key)) {
                    continue;
                }
                edges.add(key);
            }

            String callerNode = entry.callerNode(callGraphReport);
            append(TAB).append(callerNode)
                    .append(" -> ")
                    .append(entry.calleeNode());
            if (edgeShowsCallerMethod && !callerNode.contains(":")) {
                append(" [label=\"")
                        .append(entry.getCallerMethod())
                        .append("\"];");
            }
            append(NEWLINE);

        }
        return this;
    }

    private CallGraphRenderer append(String s) {
        content.append(s);
        return this;
    }
}
