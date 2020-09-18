/*
 * Copyright cp-ddd-framework Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
/**
 * 容器对插件的动态加载机制.
 * <p>
 * <p>插件包，可以是Pattern包，也可以是Partner包，都是fat jar</p>
 * <p>插件包，是可以被加载和卸载的</p>
 * <p>容器就是宿主系统，只有一个，是不可以被卸载的</p>
 */
package org.cdf.ddd.plugin;