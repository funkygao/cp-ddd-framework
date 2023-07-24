/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.maven;

import io.github.dddplus.ast.enforcer.ExtensionMethodSignatureEnforcer;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.IOException;

/**
 * 检查{@link io.github.dddplus.ext.IDomainExtension}方法签名的返回值不能primitive type，避免NPE.
 */
@Mojo(name = "ExtensionMethodSignatureEnforcer", aggregator = true)
public class ExtensionMethodSignatureEnforcerMojo extends AbstractMojo {

    /**
     * Colon separated directories.
     */
    @Parameter(property = "rootDir")
    private String rootDir;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (rootDir == null) {
            getLog().error("Usage: mvn io.github.dddplus:dddplus-maven-plugin:ExtensionMethodSignatureEnforcer -DrootDir=${colon separated dirs}");
            return;
        }

        try {
            String[] dirPaths = rootDir.split(";");
            File[] dirs = new File[dirPaths.length];
            for (int i = 0; i < dirPaths.length; i++) {
                dirs[i] = new File(dirPaths[i]);
            }
            new ExtensionMethodSignatureEnforcer()
                    .scan(dirs)
                    .enforce();

            getLog().info("ExtensionMethodSignatureEnforcer result: OK");
        } catch (IOException e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }
}
