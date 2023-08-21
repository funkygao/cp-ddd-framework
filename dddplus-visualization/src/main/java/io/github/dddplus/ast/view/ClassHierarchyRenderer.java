package io.github.dddplus.ast.view;

import io.github.dddplus.ast.model.ReverseEngineeringModel;
import io.github.dddplus.ast.report.ClassHierarchyReport;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ClassHierarchyRenderer implements IModelRenderer<ClassHierarchyRenderer> {
    private ClassHierarchyReport report;
    private String targetDotFile;
    private static final String extendsEdgeTpl = " [color=red label=\"%s\"];";
    private static final String implementssEdgeTpl = " [color=blue label=\"%s\"];";

    private StringBuilder content = new StringBuilder();

    public ClassHierarchyRenderer targetDotFile(String targetFile) {
        this.targetDotFile = targetFile;
        return this;
    }

    public ClassHierarchyRenderer ignoreParent(String parentClazz) {
        report.getIgnoredParentClasses().add(parentClazz);
        return this;
    }

    public ClassHierarchyRenderer ignores(String[] ignored) {
        for (String parent : ignored) {
            if (parent.isEmpty()) {
                continue;
            }

            ignoreParent(parent);
        }
        return this;
    }

    @Override
    public ClassHierarchyRenderer withModel(ReverseEngineeringModel model) {
        this.report = model.getClassHierarchyReport();
        return this;
    }

    @Override
    public void render() throws IOException {
        append("digraph G {")
                .append(NEWLINE)
                .append(TAB).append("rankdir=LR;").append(NEWLINE)
                .append(TAB).append("splines = polyline;").append(NEWLINE)
                .append(TAB).append("node [shape=none];").append(NEWLINE)
                .renderEdges()
                .append("}");

        File file = new File(targetDotFile);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.append(content);
        }
    }

    private ClassHierarchyRenderer renderEdges() {
        for (ClassHierarchyReport.Pair pair : report.extendsRelations()) {
            append(pair.getFrom()).append(" -> ").append(pair.getTo()).append(SPACE);
            append(String.format(extendsEdgeTpl, pair.getFromJavadoc())).append(NEWLINE);
        }
        for (ClassHierarchyReport.Pair pair : report.implementsRelations()) {
            append(pair.getFrom()).append(" -> ").append(pair.getTo()).append(SPACE);
            append(String.format(implementssEdgeTpl, pair.getFromJavadoc())).append(NEWLINE);
        }
        return this;
    }

    private ClassHierarchyRenderer append(String s) {
        content.append(s);
        return this;
    }
}
