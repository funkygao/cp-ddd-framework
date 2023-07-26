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
import java.util.ArrayList;
import java.util.List;

/**
 * 业务模型可视化.
 */
@Mojo(name = "visualize", aggregator = true)
public class ModelingVisualizationMojo extends AbstractMojo {

    /**
     * Colon separated directories.
     */
    @Parameter(property = "rootDir")
    String rootDir;

    @Parameter(property = "callGraph")
    String targetCallGraph;
    @Parameter(property = "pkgRef")
    String targetPackageRef;
    @Parameter(property = "plantUml")
    String targetPlantUml;
    @Parameter(property = "encapsulation")
    String targetEncapsulation;
    @Parameter(property = "textModel")
    String targetTextModel;
    @Parameter(property = "rawClassSimilarity")
    Boolean rawClassSimilarity = false;
    @Parameter(property = "similarityThreshold")
    Integer similarityThreshold = 70;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (rootDir == null) {
            getLog().error("Usage: mvn io.github.dddplus:dddplus-maven-plugin:visualize -DrootDir=xx -DcallGraph=xx.dot -DpkgRef=xx.dot -DplantUml=xx.svg -Dencapsulation=xxx.txt -DtextModel=xx.txt");
            return;
        }

        getLog().info("Reverse modeling starting ...");
        try {
            String[] dirPaths = rootDir.split(":");
            File[] dirs = new File[dirPaths.length];
            for (int i = 0; i < dirPaths.length; i++) {
                dirs[i] = new File(dirPaths[i]);
            }

            DomainModelAnalyzer analyzer = new DomainModelAnalyzer()
                    .scan(dirs);
            if (rawClassSimilarity) {
                analyzer.rawSimilarity()
                        .similarityThreshold(similarityThreshold);
            }
            ReverseEngineeringModel model = analyzer.analyze();

            getLog().info("Rendering ...");
            List<String> artifacts = new ArrayList<>();
            if (targetPlantUml != null) {
                artifacts.add(targetPlantUml);
                new PlantUmlRenderer()
                        .direction(PlantUmlRenderer.Direction.TopToBottom)
                        .skinParamPolyline()
                        .build(model)
                        .classDiagramSvgFilename(targetPlantUml)
                        .render();
            }
            if (targetCallGraph != null) {
                artifacts.add(targetCallGraph);
                artifacts.add(targetPackageRef);
                new CallGraphRenderer()
                        .targetCallGraphDotFile(targetCallGraph)
                        .targetPackageCrossRefDotFile(targetPackageRef)
                        .splines("polyline")
                        .build(model)
                        .render();
            }
            if (targetEncapsulation != null) {
                artifacts.add(targetEncapsulation);
                new EncapsulationRenderer()
                        .build(model)
                        .targetFilename(targetEncapsulation)
                        .render();
            }
            if (targetTextModel != null) {
                artifacts.add(targetTextModel);
                new PlainTextRenderer()
                        .showRawSimilarities()
                        .targetFilename(targetTextModel)
                        .build(model)
                        .render();
            }

            getLog().info("Reverse Modeling Executed OK");
            getLog().info("Please check out your modeling artifacts: " + String.join(", ", artifacts));
        } catch (IOException e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }

}
