package io.github.dddplus.runtime.registry.mock.policy;

import io.github.dddplus.annotation.Policy;
import io.github.dddplus.ext.IExtPolicy;
import io.github.dddplus.runtime.registry.mock.ext.ITrigger;
import io.github.dddplus.runtime.registry.mock.model.FooModel;
import lombok.extern.slf4j.Slf4j;

@Policy(extClazz = ITrigger.class)
@Slf4j
public class TriggerExtPolicy implements IExtPolicy<FooModel> {

    @Override
    public String extensionCode(FooModel model) {
        // 在此，可以调用配置中心，决定目标扩展点
        if (model.isFoo()) {
            log.info("will use foo");
            return "foo";
        }

        log.info("will use bar");
        return "bar";
    }
}
