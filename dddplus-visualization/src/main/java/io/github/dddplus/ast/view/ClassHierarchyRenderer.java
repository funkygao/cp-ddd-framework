package io.github.dddplus.ast.view;

import io.github.dddplus.ast.model.ReverseEngineeringModel;
import io.github.dddplus.ast.parser.JavaParserUtil;
import io.github.dddplus.ast.report.ClassHierarchyReport;

import java.io.IOException;

public class ClassHierarchyRenderer implements IModelRenderer<ClassHierarchyRenderer> {
    private ClassHierarchyReport report;
    private String targetDotFile;
    private static final String extendsEdgeTpl = " [color=red fontsize=10 arrowsize=0.5 edgetooltip=\"%s\" label=\"%s\"];";
    private static final String implementsEdgeTpl = " [color=blue fontsize=10 arrowhead=empty arrowsize=0.5 style=dashed edgetooltip=\"%s\" label=\"%s\"];";

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

        JavaParserUtil.dumpToFile(targetDotFile, content.toString());
    }

    private ClassHierarchyRenderer renderEdges() {
        for (ClassHierarchyReport.Pair pair : report.displayRelations()) {
            append("\"").append(pair.getFrom()).append("\"")
                    .append(" -> ")
                    .append("\"").append(pair.getTo()).append("\"")
                    .append(SPACE);
            switch (pair.getRelation()) {
                case Extends:
                    append(String.format(extendsEdgeTpl, pair.dotLabel(), pair.dotLabel()));
                    break;

                case Implements:
                    append(String.format(implementsEdgeTpl, pair.dotLabel(), pair.dotLabel()));
                    break;
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
