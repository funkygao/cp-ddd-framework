package io.github.dddplus.runtime.interceptor;

import io.github.dddplus.annotation.Interceptor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Interceptor
@Slf4j
public class LogInterceptor implements IExtensionInterceptor {
    @Override
    public void beforeInvocation(@NonNull ExtensionContext context) {
        log.info("BEFORE: {}.{}", context.getExtension().getClass().getCanonicalName(), context.getMethod().getName());
    }

    @Override
    public void afterInvocation(@NonNull ExtensionContext context) {
        log.info("AFTER: {} {}", context.getCode(), context.getArgs());
    }
}
