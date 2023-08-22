/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.maven;

import io.github.dddplus.ast.DomainModelAnalyzer;
import io.github.dddplus.ast.model.ReverseEngineeringModel;
import io.github.dddplus.ast.view.*;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
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
    @Parameter(property = "rootDir", required = true)
    String rootDir;

    @Parameter(property = "callGraph")
    String targetCallGraph;
    @Parameter(property = "pkgRef")
    String targetPackageRef;
    @Parameter(property = "plantUml")
    String targetPlantUml;
    @Parameter(property = "plantUmlSrc")
    String targetPlantUmlSrc;
    @Parameter(property = "encapsulation")
    String targetEncapsulation;
    @Parameter(property = "textModel")
    String targetTextModel;
    @Parameter(property = "rawClassSimilarity")
    Boolean rawClassSimilarity = false;
    @Parameter(property = "similarityThreshold")
    Integer similarityThreshold = 70;
    @Parameter(property = "sqliteDb")
    String sqliteDb;
    @Parameter(property = "fixModelPkg")
    String keyModelPkgFix;
    @Parameter(property = "classHierarchy")
    String classHierarchy;
    /**
     * Colon separated ignored parent classes.
     */
    @Parameter(property = "classHierarchyIgnoreParents")
    String classHierarchyIgnoreParents;


    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("Reverse modeling starting ...");
        try {
            String[] dirPaths = rootDir.split(":");
            File[] dirs = new File[dirPaths.length];
            for (int i = 0; i < dirPaths.length; i++) {
                dirs[i] = new File(dirPaths[i]);
            }

            DomainModelAnalyzer analyzer = new DomainModelAnalyzer()
                    .scan(dirs);
            if (keyModelPkgFix != null) {
                for (String pair : keyModelPkgFix.split(",")) {
                    String[] pkgPair = pair.split(":");
                    analyzer.fixKeyModelPackage(pkgPair[0], pkgPair[1]);
                }
            }
            if (targetCallGraph == null) {
                analyzer.disableCallGraph();
            }
            if (rawClassSimilarity) {
                analyzer.rawSimilarity()
                        .similarityThreshold(similarityThreshold);
            }
            ReverseEngineeringModel model = analyzer.analyze();

            getLog().info("Rendering ...");
            List<String> artifacts = new ArrayList<>();
            if (targetPlantUml != null) {
                artifacts.add(targetPlantUml);
                PlantUmlRenderer renderer = new PlantUmlRenderer()
                        .withModel(model)
                        .direction(PlantUmlRenderer.Direction.TopToBottom)
                        .skinParamPolyline()
                        .classDiagramSvgFilename(targetPlantUml);
                if (targetPlantUmlSrc != null) {
                    renderer.plantUmlFilename(targetPlantUmlSrc);
                    artifacts.add(targetPlantUmlSrc);
                }
                renderer.render();
            }
            if (targetCallGraph != null) {
                artifacts.add(targetCallGraph);
                artifacts.add(targetPackageRef);
                new CallGraphRenderer()
                        .withModel(model)
                        .targetCallGraphDotFile(targetCallGraph)
                        .targetPackageCrossRefDotFile(targetPackageRef)
                        .splines("polyline")
                        .render();
            }
            if (targetEncapsulation != null) {
                artifacts.add(targetEncapsulation);
                new EncapsulationRenderer()
                        .withModel(model)
                        .targetFilename(targetEncapsulation)
                        .render();
            }
            if (targetTextModel != null) {
                artifacts.add(targetTextModel);
                new PlainTextRenderer()
                        .withModel(model)
                        .showRawSimilarities()
                        .targetFilename(targetTextModel)
                        .render();
            }
            if (classHierarchy != null) {
                artifacts.add(classHierarchy);
                if (classHierarchyIgnoreParents == null) {
                    classHierarchyIgnoreParents = "";
                }
                new ClassHierarchyRenderer()
                        .withModel(model)
                        .ignores(classHierarchyIgnoreParents.split(","))
                        .targetDotFile(classHierarchy)
                        .render();
            }

            getLog().info("Reverse Modeling Executed OK");
            getLog().info("Please check out your modeling artifacts: " + String.join(", ", artifacts));

            if (sqliteDb != null) {
                getLog().info("Dump model to sqlite:" + sqliteDb);
                model.dump(sqliteDb);
                getLog().info("Please check out your model in sqlite:" + sqliteDb);
            }
        } catch (Exception e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }

}
