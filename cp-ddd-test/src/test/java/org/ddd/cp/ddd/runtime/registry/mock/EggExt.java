package org.ddd.cp.ddd.runtime.registry.mock;

import org.ddd.cp.ddd.runtime.registry.mock.ext.IFooExt;
import org.ddd.cp.ddd.runtime.registry.mock.model.FooModel;
import lombok.extern.slf4j.Slf4j;

// EggExt，不会被加载，因为没有加@Extension注解
// FooPattern下IFooExt已经挂了FooExt实例了，不能再挂EggExt了：否则启动检查抛出BootstrapException

//@Extension(code = FooPattern.CODE, name = "egg,属于FooPattern", jsfAlias = "    ")
@Slf4j
public class EggExt implements IFooExt {
    public static final int RESULT = 3;

    @Override
    public Integer execute(FooModel model) {
        log.info("3");
        return RESULT;
    }
}
