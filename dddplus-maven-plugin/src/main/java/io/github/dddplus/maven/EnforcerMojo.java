/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.maven;

import io.github.dddplus.DDDPlusEnforcer;
import io.github.dddplus.ast.enforcer.AllowedAccessorsEnforcer;
import io.github.dddplus.ast.enforcer.ExtensionMethodSignatureEnforcer;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.IOException;

/**
 * 确保{@link io.github.dddplus.DDDPlusEnforcer}, {@link AllowedAccessorsEnforcer}，{@link ExtensionMethodSignatureEnforcer}规范被执行.
 */
@Mojo(name = "enforce", aggregator = true)
public class EnforcerMojo extends AbstractMojo {

    @Parameter(property = "rootPackage", required = true)
    private String rootPackage;

    /**
     * Colon separated directories.
     */
    @Parameter(property = "rootDir", required = true)
    private String rootDir;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        new DDDPlusEnforcer()
                .scanPackages(rootPackage)
                .enforce();
        getLog().info("DDDPlusEnforcer OK");

        try {
            String[] dirPaths = rootDir.split(":");
            File[] dirs = new File[dirPaths.length];
            for (int i = 0; i < dirPaths.length; i++) {
                dirs[i] = new File(dirPaths[i]);
            }

            new ExtensionMethodSignatureEnforcer()
                    .scan(dirs)
                    .enforce();
            getLog().info("ExtensionMethodSignatureEnforcer OK");

            new AllowedAccessorsEnforcer()
                    .scan(dirs)
                    .enforce(null);
            getLog().info("AllowedAccessorsEnforcer OK");
        } catch (IOException e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }
}
