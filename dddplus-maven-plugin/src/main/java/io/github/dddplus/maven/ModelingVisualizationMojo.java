/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.maven;

import io.github.dddplus.ast.DomainModelAnalyzer;
import io.github.dddplus.ast.model.ReverseEngineeringModel;
import io.github.dddplus.ast.view.CallGraphRenderer;
import io.github.dddplus.ast.view.PlantUmlRenderer;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.IOException;

/**
 * 业务模型可视化：逆向业务模型，Call graph.
 */
@Mojo(name = "ModelingVisualization", aggregator = true)
public class ModelingVisualizationMojo extends AbstractMojo {

    /**
     * Colon separated directories.
     */
    @Parameter(property = "rootDir")
    private String rootDir;

    @Parameter(property = "callGraph")
    private String targetCallGraph;
    @Parameter(property = "plantUml")
    private String targetPlantUml;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (rootDir == null || targetCallGraph == null || targetCallGraph == null) {
            getLog().error("Usage: mvn io.github.dddplus:dddplus-maven-plugin:ModelingVisualization -DrootDir=xx -DcallGraph=xx -DplantUml=xx");
            return;
        }

        try {
            String[] dirPaths = rootDir.split(";");
            File[] dirs = new File[dirPaths.length];
            for (int i = 0; i < dirPaths.length; i++) {
                dirs[i] = new File(dirPaths[i]);
            }

            ReverseEngineeringModel model = new DomainModelAnalyzer()
                    .scan(dirs)
                    .analyze();
            new PlantUmlRenderer()
                    .direction(PlantUmlRenderer.Direction.TopToBottom)
                    .skinParamPolyline()
                    .build(model)
                    .classDiagramSvgFilename(targetPlantUml)
                    .render();
            new CallGraphRenderer()
                    .targetDotFilename(targetCallGraph)
                    .splines("polyline")
                    .build(model)
                    .render();

            getLog().info("dot -Tsvg callgraph.dot -o callgraph.svg");
        } catch (IOException e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }
}
