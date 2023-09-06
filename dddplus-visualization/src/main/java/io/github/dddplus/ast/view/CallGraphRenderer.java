/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast.view;

import io.github.dddplus.ast.model.CallGraphEntry;
import io.github.dddplus.ast.parser.JavaParserUtil;
import io.github.dddplus.ast.report.CallGraphReport;
import lombok.Getter;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.util.*;

/**
 * DSL -> Reverse Engineering Model -> method call -> dot DSL.
 */
public class CallGraphRenderer implements IRenderer {
    private CallGraphReport callGraphReport;
    private String targetCallGraphDotFile;

    private String splines = null;
    private StringBuilder content = new StringBuilder();
    private Map<String, Integer> calleeRefCounter = new HashMap<>();
    private Map<String, Integer> calleeMethodRefCounter = new HashMap<>();
    @Getter
    private int nodes, edges;

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

    public List<Pair<String, Integer>> topReferencedCallee(int k) {
        return topKByValue(calleeRefCounter, k);
    }

    public List<Pair<String, Integer>> topReferencedCalleeMethods(int k) {
        return topKByValue(calleeMethodRefCounter, k);
    }

    private List<Pair<String, Integer>> topKByValue(Map<String, Integer> map, int k) {
        List<Map.Entry<String, Integer>> sortedList = new ArrayList<>(map.entrySet());
        sortedList.sort(Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder()));
        List<Pair<String, Integer>> result = new ArrayList<>(k);
        int n = 0;
        for (Map.Entry<String, Integer> entry : sortedList) {
            result.add(Pair.of(entry.getKey(), entry.getValue()));
            n++;
            if (n == k) {
                break;
            }
        }

        return result;
    }

    public CallGraphRenderer withReport(CallGraphReport report) {
        this.callGraphReport = report;
        return this;
    }

    public CallGraphRenderer render() throws IOException {
        if (targetCallGraphDotFile != null) {
            renderCallGraph();
        }
        return this;
    }

    private void renderCallGraph() throws IOException {
        callGraphReport.preRender();
        append("digraph G {")
                .append(NEWLINE)
                .setupSkin()
                .renderLegend()
                .renderNodes()
                .append(NEWLINE)
                .renderEdges()
                .renderUserDefinedEdges()
                .append("}");

        JavaParserUtil.dumpToFile(targetCallGraphDotFile, content.toString());
    }

    private CallGraphRenderer renderLegend() {
        return this;
    }

    private CallGraphRenderer setupSkin() {
        append(TAB).append("rankdir=LR;").append(NEWLINE);
        if (splines != null) {
            append(TAB).append(String.format("splines = %s;", splines)).append(NEWLINE);
        }
        append(TAB).append("node [shape=Mrecord];").append(NEWLINE);
        append(TAB).append(String.format("nodesep=%.1f;", callGraphReport.getConfig().nodesep())).append(NEWLINE);
        append(TAB).append("edge [style=dashed, arrowsize=0.3, fontsize=6];").append(NEWLINE);
        append(NEWLINE);
        return this;
    }

    private CallGraphRenderer appendEscape(String s) {
        return append("\"").append(s).append("\"");
    }

    private CallGraphRenderer renderNodes() {
        Map<String, CallGraphReport.Record> mergedNodes = callGraphReport.displayNodes();
        for (String clazz : mergedNodes.keySet()) {
            nodes++;
            CallGraphReport.Record record = mergedNodes.get(clazz);
            append(TAB).appendEscape(record.dotNode())
                    .append(" [label=\"");
            List<String> list = new ArrayList<>();
            list.add(String.format("<%s> %s", record.dotNode(), record.dotNode()));

            for (String method : record.getMethods()) {
                list.add(String.format("<%s> %s", method, method));
            }
            append(String.join("|", list));
            append("\"");
            if (callGraphReport.getConfig().isUseCaseLayerClass(clazz)) {
                append(" color=red");
            }
            if (callGraphReport.getConfig().isAclClass(clazz)) {
                append(" color=green");
            }
            if (callGraphReport.getConfig().isExternalDependentClass(clazz)) {
                append(" color=blue");
            }
            append("];").append(NEWLINE);
        }

        return this;
    }

    private CallGraphRenderer renderEdges() {
        Set<String> edges = new HashSet<>();
        for (CallGraphEntry entry : callGraphReport.sortedEntries()) {
            this.edges++;

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

            // statistics
            incrementCounter(entry.getCalleeClazz(), entry.getCalleeMethod());
        }

        return this;
    }

    private CallGraphRenderer renderUserDefinedEdges() {
        for (Pair<String, String> edge : callGraphReport.getConfig().userDefinedRelations()) {
            if (!callGraphReport.containsNode(edge.getLeft())
                    || !callGraphReport.containsNode(edge.getRight())) {
                continue;
            }

            append(TAB).append(edge.getLeft()).append(" -> ").append(edge.getRight())
                    .append(NEWLINE);
        }
        
        return this;
    }

    private void incrementCounter(String calleeClazz, String calleeMethod) {
        if (!calleeRefCounter.containsKey(calleeClazz)) {
            calleeRefCounter.put(calleeClazz, 0);
        }
        calleeRefCounter.put(calleeClazz, 1 + calleeRefCounter.get(calleeClazz));

        if (!calleeMethodRefCounter.containsKey(calleeMethod)) {
            calleeMethodRefCounter.put(calleeMethod, 0);
        }
        calleeMethodRefCounter.put(calleeMethod, 1 + calleeMethodRefCounter.get(calleeMethod));
    }

    private CallGraphRenderer append(String s) {
        content.append(s);
        return this;
    }
}
