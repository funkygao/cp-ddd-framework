/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.plugin;

/**
 * 插件.
 */
public interface IPlugin {

    /**
     * 获取插件码.
     * <p>
     * <p>目前，对应的是{@code Pattern.code} 或 {@code Partner.code}.</p>
     * <p>Unique within a single JVM.</p>
     */
    String getCode();

    /**
     * 获取当前版本.
     */
    String getVersion();
}
