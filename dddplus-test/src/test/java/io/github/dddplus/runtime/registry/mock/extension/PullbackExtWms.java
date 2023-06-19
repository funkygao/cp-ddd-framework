package io.github.dddplus.runtime.registry.mock.extension;

import io.github.dddplus.annotation.Extension;
import io.github.dddplus.runtime.registry.mock.ext.IPullbackExt;
import io.github.dddplus.runtime.registry.mock.pattern.WmsPattern;
import lombok.extern.slf4j.Slf4j;

@Extension(code = WmsPattern.CODE)
@Slf4j
public class PullbackExtWms implements IPullbackExt {
    @Override
    public void pullback() {
        log.info("WMS pullback");
    }
}
