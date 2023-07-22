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

import java.io.File;

/**
 * 确保{@link io.github.dddplus.model.encapsulation.AllowedAccessors}规范执行.
 * <p>
 * <p>Usage:</p>
 * {@code mvn io.github.dddplus:dddplus-maven-plugin:AllowedAccessorsEnforcer}
 */
@Mojo(name = "AllowedAccessorsEnforcer", aggregator = true)
public class AllowedAccessorsEnforcerMojo extends AbstractMojo {

    @Parameter(property = "rootDir")
    private File rootDir;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        new AllowedAccessorsEnforcer()
                .scan(rootDir)
                .enforce(null);

    }
}
