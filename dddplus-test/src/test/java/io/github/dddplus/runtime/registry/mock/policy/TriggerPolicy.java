package io.github.dddplus.runtime.registry.mock.policy;

import io.github.dddplus.annotation.Governance;
import io.github.dddplus.annotation.Policy;
import io.github.dddplus.ext.IPolicy;
import io.github.dddplus.runtime.registry.mock.ext.ITrigger;
import io.github.dddplus.runtime.registry.mock.model.FooModel;
import lombok.extern.slf4j.Slf4j;

@Policy
@Slf4j
public class TriggerPolicy implements IPolicy<ITrigger, FooModel> {

    @Override
    @Governance
    public String extensionCode(FooModel model) {
        // 在此，可以调用配置中心，决定目标扩展点
        switch (model.getFoo()) {
            case 1:
                log.info("will use foo");
                return "foo";

            case 2:
                log.info("will use bar");
                return "bar";

            case 3:
                log.info("will return invalid extensionCode");
                return "invalid";

            default:
                log.info("invalid");
                return null;
        }
    }
}
