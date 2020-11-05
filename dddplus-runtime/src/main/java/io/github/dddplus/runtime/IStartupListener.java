/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.runtime;

/**
 * 系统启动监听器.
 */
public interface IStartupListener {

    /**
     * 启动时执行.
     * <p>
     * <p>触发时机：Spring加载完毕后</p>
     */
    void onStartComplete();
}
