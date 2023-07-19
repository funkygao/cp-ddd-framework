package io.github.dddplus.ast.view;

import guru.nidi.graphviz.attribute.Rank;
import guru.nidi.graphviz.attribute.Style;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Factory;
import guru.nidi.graphviz.model.MutableGraph;
import io.github.dddplus.ast.ReverseEngineeringModel;
import io.github.dddplus.ast.model.CallGraphEntry;
import io.github.dddplus.ast.report.CallGraphReport;

import java.io.File;
import java.io.IOException;

import static guru.nidi.graphviz.attribute.Rank.RankDir.LEFT_TO_RIGHT;
import static guru.nidi.graphviz.model.Factory.mutNode;

public class GraphvizRenderer implements IModelRenderer<GraphvizRenderer> {
    private CallGraphReport callGraphReport;
    private String targetFile;

    public GraphvizRenderer targetFile(String targetFile) {
        this.targetFile = targetFile;
        return this;
    }

    @Override
    public GraphvizRenderer build(ReverseEngineeringModel model) {
        this.callGraphReport = model.getCallGraphReport();
        return this;
    }

    @Override
    public void render() throws IOException {
        MutableGraph g = Factory.mutGraph("CallGraph")
                .setDirected(true)
                .setCluster(true)
                .graphAttrs().add(Rank.dir(LEFT_TO_RIGHT));
        for (CallGraphEntry entry : callGraphReport.getEntries()) {
            g.add(mutNode(entry.getCallerClazz())
                    .add(Style.DASHED)
                    .addLink(mutNode(entry.calleeNode())));
        }
        Graphviz.fromGraph(g).render(Format.SVG).toFile(new File(targetFile));
    }
}
