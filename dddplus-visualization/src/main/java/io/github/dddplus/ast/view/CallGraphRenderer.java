package io.github.dddplus.ast.view;

import io.github.dddplus.ast.ReverseEngineeringModel;
import io.github.dddplus.ast.model.CallGraphEntry;
import io.github.dddplus.ast.report.CallGraphReport;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CallGraphRenderer implements IModelRenderer<CallGraphRenderer> {
    private CallGraphReport callGraphReport;
    private String targetDotFilename;
    private boolean edgeShowsCallerMethod = false;

    public CallGraphRenderer targetDotFilename(String targetFile) {
        this.targetDotFilename = targetFile;
        return this;
    }

    public CallGraphRenderer edgeShowsCallerMethod() {
        this.edgeShowsCallerMethod = true;
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
        content.append(TAB).append("label=\"Call Graph\";").append(NEWLINE);
        content.append(TAB).append("labelloc = \"t\";").append(NEWLINE);
        content.append(TAB).append("rankdir=LR;").append(NEWLINE);

        for (CallGraphEntry entry : callGraphReport.getEntries()) {
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
