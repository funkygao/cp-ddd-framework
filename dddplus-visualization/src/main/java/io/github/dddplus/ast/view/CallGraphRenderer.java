package io.github.dddplus.ast.view;

import io.github.dddplus.ast.ReverseEngineeringModel;
import io.github.dddplus.ast.model.CallGraphEntry;
import io.github.dddplus.ast.report.CallGraphReport;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CallGraphRenderer implements IModelRenderer<CallGraphRenderer> {
    private CallGraphReport callGraphReport;
    private String targetDotFilename;
    private boolean edgeShowsCallerMethod = false;
    private String splines = null;

    public CallGraphRenderer targetDotFilename(String targetFile) {
        this.targetDotFilename = targetFile;
        return this;
    }

    public CallGraphRenderer edgeShowsCallerMethod() {
        this.edgeShowsCallerMethod = true;
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
    public CallGraphRenderer build(ReverseEngineeringModel model) {
        this.callGraphReport = model.getCallGraphReport();
        return this;
    }

    @Override
    public void render() throws IOException {
        StringBuilder content = new StringBuilder();
        content.append("digraph G {").append(NEWLINE);
        content.append(TAB).append("labelloc = \"t\";").append(NEWLINE);
        content.append(TAB).append("rankdir=LR;").append(NEWLINE);
        if (splines != null) {
            content.append(TAB).append(String.format("splines = %s;", splines)).append(NEWLINE);
        }
        content.append(TAB).append("node [shape=record];").append(NEWLINE);
        content.append(TAB).append("edge [style = dashed, fontsize=10];").append(NEWLINE);
        content.append(NEWLINE);

        for (CallGraphReport.Record calleeClazz : callGraphReport.calleeRecords()) {
            content.append(TAB).append(calleeClazz.getClazz())
                    .append(" [label=\"");
            List<String> list = new ArrayList<>();
            list.add(String.format("<%s> %s", calleeClazz.getClazz(), calleeClazz.getClazz()));
            for (String method : calleeClazz.getMethods()) {
                list.add(String.format("<%s> %s", method, method));
            }
            content.append(String.join("|", list));
            content.append("\"];").append(NEWLINE);
        }

        content.append(NEWLINE);

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

            content.append(TAB).append(entry.getCallerClazz()).append(" -> ")
                    .append(entry.calleeNode());
            if (edgeShowsCallerMethod) {
                content.append(" [label=\"")
                        .append(entry.getCallerMethod())
                        .append("\"];");
            }
            content.append(NEWLINE);
        }
        content.append("}");

        File file = new File(targetDotFilename);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.append(content);
        }

        // dot -Tsvg a.dot -o a.svg
    }
}
