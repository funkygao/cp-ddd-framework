package org.ddd.cp.ddd.runtime.registry.mock;

import lombok.extern.slf4j.Slf4j;
import org.ddd.cp.ddd.annotation.Extension;
import org.ddd.cp.ddd.runtime.registry.mock.ext.IFooExt;
import org.ddd.cp.ddd.runtime.registry.mock.model.FooModel;
import org.ddd.cp.ddd.runtime.registry.mock.plugin.XxxExtPlugin;
import org.ddd.cp.ddd.runtime.registry.mock.project.FooProject;

@Extension(code = FooProject.CODE, name = "垂直业务实现的扩展点Foo项目，同时也展示JSF形式扩展点如何使用")
@Slf4j
public class ProjectExt implements IFooExt {
    public static final int RESULT = 19;

    //@Resource
    private XxxExtPlugin xxxExtPlugin;

    @Override
    public Integer execute(FooModel model) {
        log.info("project");
        if (false) {
            // 通过JSF调用前台扩展点，cp-core框架会根据jsfAlias配置自动定位到该调用哪一个扩展点提供者
            // 原理是利用了JSF的ConsumerGroup分组调用机制：https://cf.jd.com/pages/viewpage.action?pageId=254686696

            // 中台通过JSF方式实现扩展点后，domain model是无法被前台使用的，只能靠DTO传递
            // 这就要求中台domain层通过translator把model转换为JSF扩展点方法的入参DTO，同时把出参放回到model
            // spec包里的dict/ext/model等，JSF扩展点实现者都无法使用，相当于只依赖plugin包，不依赖spec包
            // plugin包和spec包互不依赖
            // 二方包形式的扩展点相当于邻居家的孩子，至少中台还要照顾一下；JSF形式的扩展点相当于野孩子，放任自流

            // 此外，也面临一个问题：JSF扩展点是一个接口一个方法，还是一个接口多个方法？但前台不想提供某个方法的实现怎么办？
            // 由于扩展点的定位是基于接口粒度的，无法做到方法级别，因此某一个前台如果要实现一个JSF扩展点接口，那么他就要认真实现其中每一个方法
            // JSF的长连接和心跳线程维护(每一个长连接对应3个线程)是基于接口的，接口越多资源消耗越多，因此一个JSF扩展点一个方法是不可取的

            // JSF形式扩展点，唯一的独特好处：前中台上线过程解耦，双方各上各的线
            // 但如果前台上线过程无法做到graceful shutdown，也会造成中台服务的不稳定
            // 其他的问题，例如jar冲突、前台把中台拖死等，二方包形式扩展点已经解决了

            try {
                Integer result = xxxExtPlugin.doSth(1, 2);
                if (result == null) {
                    throw new RuntimeException("Unexpected plugin result");
                }

                if (result == 1) {
                    model.setB2c(true);
                }
            } catch (RuntimeException ex) {
                // 处理各种异常
            }
        }
        return RESULT;
    }
}
