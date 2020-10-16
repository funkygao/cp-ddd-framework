package io.github.dddplus.runtime.registry.mock.plugin;

/**
 * 中台定义的Dubbo形式的扩展点：中台定义接口，前台来部署服务器实现它
 */
public interface IXxxExtPlugin {

    Integer doSth(Integer a, Integer b);
}
