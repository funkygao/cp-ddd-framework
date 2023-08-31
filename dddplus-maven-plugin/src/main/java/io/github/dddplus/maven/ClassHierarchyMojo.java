/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.maven;

import io.github.dddplus.ast.DomainModelAnalyzer;
import io.github.dddplus.ast.model.ReverseEngineeringModel;
import io.github.dddplus.ast.view.ClassHierarchyRenderer;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;

/**
 * 无需标注，类的多态结构可视化.
 */
@Mojo(name = "polymorphism", aggregator = true)
public class ClassHierarchyMojo extends AbstractMojo {

    /**
     * Colon separated directories.
     */
    @Parameter(property = "rootDir", required = true)
    String rootDir;

    @Parameter(property = "targetFile", required = true)
    String targetFile;

    /**
     * ortho | spline | polyline | curved
     */
    @Parameter(property = "splines", defaultValue = "curved")
    String splines;

    /**
     * Comma separated ignored parent classes.
     */
    @Parameter(property = "ignoreParents")
    String ignoreParents;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("Reverse modeling starting ...");
        try {
            String[] dirPaths = rootDir.split(":");
            File[] dirs = new File[dirPaths.length];
            for (int i = 0; i < dirPaths.length; i++) {
                dirs[i] = new File(dirPaths[i]);
            }

            ReverseEngineeringModel model = new DomainModelAnalyzer()
                    .scan(dirs)
                    .classHierarchyOnly()
                    .analyze();
            getLog().info("Rendering ...");
            if (ignoreParents == null) {
                ignoreParents = "";
            }
            new ClassHierarchyRenderer()
                    .withModel(model)
                    .ignores(ignoreParents.split(","))
                    .targetDotFile(targetFile)
                    .splines(splines)
                    .render();

            getLog().info("Reverse Modeling Executed OK");
        } catch (Exception e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }

}
