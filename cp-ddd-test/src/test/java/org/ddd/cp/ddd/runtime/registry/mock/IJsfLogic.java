package org.ddd.cp.ddd.runtime.registry.mock;

// 扩展点的实现逻辑通过JSF服务来完成，而不是二方包
// 通常，这个接口定义在中台的plugin包(e,g. cp-oms-doo-plugin)，前台业务引入并实现和部署系统
public interface IJsfLogic {

    String doSth(String arg);

}
