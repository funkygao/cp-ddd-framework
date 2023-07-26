/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.maven;

import io.github.dddplus.ast.DomainModelAnalyzer;
import io.github.dddplus.ast.model.ReverseEngineeringModel;
import io.github.dddplus.ast.view.CallGraphRenderer;
import io.github.dddplus.ast.view.EncapsulationRenderer;
import io.github.dddplus.ast.view.PlainTextRenderer;
import io.github.dddplus.ast.view.PlantUmlRenderer;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.IOException;

/**
 * 业务模型可视化.
 */
@Mojo(name = "visualize", aggregator = true)
public class ModelingVisualizationMojo extends AbstractMojo {

    /**
     * Colon separated directories.
     */
    @Parameter(property = "rootDir")
    private String rootDir;

    @Parameter(property = "callGraph")
    private String targetCallGraph;
    @Parameter(property = "pkgRef")
    private String targetPackageRef;
    @Parameter(property = "plantUml")
    private String targetPlantUml;
    @Parameter(property = "encapsulation")
    private String targetEncapsulation;
    @Parameter(property = "textModel")
    private String targetTextModel;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (rootDir == null) {
            getLog().error("Usage: mvn io.github.dddplus:dddplus-maven-plugin:visualize -DrootDir=xx -DcallGraph=xx.dot -DpkgRef=xx.dot -DplantUml=xx.svg -Dencapsulation=xxx.txt -DtextModel=xx.txt");
            return;
        }

        try {
            String[] dirPaths = rootDir.split(":");
            File[] dirs = new File[dirPaths.length];
            for (int i = 0; i < dirPaths.length; i++) {
                dirs[i] = new File(dirPaths[i]);
            }

            ReverseEngineeringModel model = new DomainModelAnalyzer()
                    .scan(dirs)
                    .analyze();
            if (targetPlantUml != null) {
                new PlantUmlRenderer()
                        .direction(PlantUmlRenderer.Direction.TopToBottom)
                        .skinParamPolyline()
                        .build(model)
                        .classDiagramSvgFilename(targetPlantUml)
                        .render();
            }
            if (targetCallGraph != null) {
                new CallGraphRenderer()
                        .targetCallGraphDotFile(targetCallGraph)
                        .targetPackageCrossRefDotFile(targetPackageRef)
                        .splines("polyline")
                        .build(model)
                        .render();
            }
            if (targetEncapsulation != null) {
                new EncapsulationRenderer()
                        .build(model)
                        .targetFilename(targetEncapsulation)
                        .render();
            }
            if (targetTextModel != null) {
                new PlainTextRenderer()
                        .showRawSimilarities()
                        .targetFilename(targetTextModel)
                        .build(model)
                        .render();
            }

            getLog().info("OK");
        } catch (IOException e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }

}
