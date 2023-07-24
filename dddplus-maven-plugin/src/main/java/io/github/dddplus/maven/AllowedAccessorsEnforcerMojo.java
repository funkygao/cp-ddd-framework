/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.maven;

import io.github.dddplus.ast.enforcer.AllowedAccessorsEnforcer;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;

/**
 * 确保{@link io.github.dddplus.model.encapsulation.AllowedAccessors}规范执行.
 */
@Mojo(name = "AllowedAccessorsEnforcer", aggregator = true)
public class AllowedAccessorsEnforcerMojo extends AbstractMojo {

    /**
     * Colon separated directories.
     */
    @Parameter(property = "rootDir")
    private String rootDir;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (rootDir == null) {
            getLog().error("Usage: mvn io.github.dddplus:dddplus-maven-plugin:AllowedAccessorsEnforcer -DrootDir=${colon separated dirs}");
            return;
        }

        String[] dirPaths = rootDir.split(";");
        File[] dirs = new File[dirPaths.length];
        for (int i = 0; i < dirPaths.length; i++) {
            dirs[i] = new File(dirPaths[i]);
        }
        new AllowedAccessorsEnforcer()
                .scan(dirs)
                .enforce(null);

        getLog().info("AllowedAccessorsEnforcer result: OK");
    }
}
