package org.cdf.ddd.runtime.registry.mock.plugin;

/**
 * 中台定义的Dubbo形式的扩展点：中台定义接口，前台来部署服务器实现它
 */
public interface XxxExtPlugin {

    Integer doSth(Integer a, Integer b);
}
