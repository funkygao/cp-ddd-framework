package io.github.dddplus.runtime.registry.mock.extension;

import io.github.dddplus.annotation.Extension;
import io.github.dddplus.runtime.registry.mock.ext.IPullbackExt;
import io.github.dddplus.runtime.registry.mock.pattern.TmsPattern;
import lombok.extern.slf4j.Slf4j;

@Extension(code = TmsPattern.CODE)
@Slf4j
public class PullbackExtTms implements IPullbackExt {
    @Override
    public void pullback() {
        log.info("TMS pullback");
    }
}
