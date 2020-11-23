package io.github.dddplus.runtime.registry.mock.extension;

import io.github.dddplus.annotation.Extension;
import io.github.dddplus.runtime.registry.mock.ext.ITrigger;
import io.github.dddplus.runtime.registry.mock.model.FooModel;
import lombok.extern.slf4j.Slf4j;

@Extension(code = "bar")
@Slf4j
public class BarTriggerExt implements ITrigger {
    
    @Override
    public void beforeInsert(FooModel model) {
        log.info("bar trigger");
    }

    @Override
    public void afterInsert(FooModel model) {
        log.info("after insert: {}", model);
    }

}
