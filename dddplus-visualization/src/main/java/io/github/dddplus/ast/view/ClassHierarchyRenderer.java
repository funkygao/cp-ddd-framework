package io.github.dddplus.ast.view;

import io.github.dddplus.ast.model.ReverseEngineeringModel;
import io.github.dddplus.ast.parser.JavaParserUtil;
import io.github.dddplus.ast.report.ClassHierarchyReport;

import java.io.IOException;

import static io.github.dddplus.ast.report.ClassHierarchyReport.Pair.Relation.Extends;
import static io.github.dddplus.ast.report.ClassHierarchyReport.Pair.Relation.Implements;

public class ClassHierarchyRenderer implements IModelRenderer<ClassHierarchyRenderer> {
    private ClassHierarchyReport report;
    private String targetDotFile;
    private String splines = "curved";
    private static final String implementsEdgeStyle = "[arrowhead=empty style=dashed label=\"%s\"]";

    private StringBuilder content = new StringBuilder();

    public ClassHierarchyRenderer targetDotFile(String targetFile) {
        this.targetDotFile = targetFile;
        return this;
    }

    public ClassHierarchyRenderer splines(String value) {
        this.splines = value;
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
                .append("fontname=\"Helvetica,Arial,sans-serif\"\n" +
                        "node [fontname=\"Helvetica,Arial,sans-serif\"]\n" +
                        "edge [fontname=\"Helvetica,Arial,sans-serif\"]\n" +
                        "splines=" + splines + "\n" +
                        "rankdir=\"LR\"\n" +
                        "node [shape=box, height=0.25]\n" +
                        "edge [fontsize=8 arrowsize=0.5]").append(NEWLINE)
                .renderEdges()
                .append("}");

        JavaParserUtil.dumpToFile(targetDotFile, content.toString());
    }

    private ClassHierarchyRenderer renderEdges() {
        for (ClassHierarchyReport.Pair pair : report.displayRelations()) {
            append("\"").append(pair.dotFrom()).append("\"")
                    .append(" -> ")
                    .append("\"").append(pair.dotTo()).append("\"");
            if (pair.getRelation() == Implements) {
                append(SPACE).append(String.format(implementsEdgeStyle, pair.displayGenericTypes()));
            }
            if (pair.getRelation() == Extends) {
                append(SPACE).append(String.format("[label=\"%s\"]", pair.displayGenericTypes()));
            }
            append(NEWLINE);
        }
        return this;
    }

    private ClassHierarchyRenderer append(String s) {
        content.append(s);
        return this;
    }
}
