package io.github.dddplus.ast.view;

import guru.nidi.graphviz.attribute.Font;
import guru.nidi.graphviz.attribute.Rank;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.Node;
import io.github.dddplus.ast.ReverseEngineeringModel;
import io.github.dddplus.ast.model.AggregateEntry;
import lombok.Builder;

import java.io.File;
import java.io.IOException;

import static guru.nidi.graphviz.attribute.Rank.RankDir.LEFT_TO_RIGHT;
import static guru.nidi.graphviz.model.Factory.graph;
import static guru.nidi.graphviz.model.Factory.node;

@Builder
public class GraphvizRender {
    private String title;
    private int height = 100;

    public void render(ReverseEngineeringModel model, String pngFileName) throws IOException {
        Graph g = graph(title).directed()
                .graphAttr().with(Rank.dir(LEFT_TO_RIGHT))
                .nodeAttr().with(Font.name("arial"))
                .linkAttr().with("class", "link-class");
        for (AggregateEntry aggregateEntry : model.getAggregateReport().getAggregateEntries()) {
            Node node = node(aggregateEntry.getName());

        }

        Graphviz.fromGraph(g).height(height)
                .render(Format.PNG)
                .toFile(new File(pngFileName));
    }
}
