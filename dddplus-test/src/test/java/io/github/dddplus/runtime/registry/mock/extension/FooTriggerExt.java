package io.github.dddplus.runtime.registry.mock.extension;

import io.github.dddplus.annotation.Extension;
import io.github.dddplus.runtime.registry.mock.ext.ITrigger;
import io.github.dddplus.runtime.registry.mock.model.FooModel;
import lombok.extern.slf4j.Slf4j;

@Extension(code = "foo")
@Slf4j
public class FooTriggerExt implements ITrigger {

    @Override
    public void beforeInsert(FooModel model) {
        log.info("foo trigger");
    }

    @Override
    public void afterInsert(FooModel model) {
        log.info("after insert: {}", model);
    }
}
