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

import java.io.File;
import java.io.IOException;

/**
 * 检查{@link io.github.dddplus.ext.IDomainExtension}方法签名的返回值不能primitive type，避免NPE.
 * <p>
 * <p>Usage:</p>
 * {@code mvn io.github.dddplus:dddplus-maven-plugin:ExtensionMethodSignatureEnforcer}
 */
//@Mojo(name = "ExtensionMethodSignatureEnforcer", aggregator = true)
public class ExtensionMethodSignatureEnforcerMojo extends AbstractMojo {

    @Parameter(property = "rootDir")
    private File rootDir;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            new ExtensionMethodSignatureEnforcer()
                    .scan(rootDir)
                    .enforce();
        } catch (IOException e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }
}
