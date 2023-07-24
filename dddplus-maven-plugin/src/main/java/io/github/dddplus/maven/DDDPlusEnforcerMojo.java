/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.maven;

import io.github.dddplus.DDDPlusEnforcer;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * 确保{@link io.github.dddplus.DDDPlusEnforcer}规范执行.
 */
@Mojo(name = "DDDPlusEnforcer", aggregator = true)
public class DDDPlusEnforcerMojo extends AbstractMojo {

    @Parameter(property = "rootPackage")
    private String rootPackage;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (rootPackage == null) {
            getLog().error("Usage: mvn io.github.dddplus:dddplus-maven-plugin:DDDPlusEnforcer -DrootPackage=${pkgname}");
            return;
        }

        new DDDPlusEnforcer()
                .scanPackages(rootPackage)
                .enforce();

        getLog().info("DDDPlusEnforcer result: OK");
    }
}
