package io.github.dddplus.runtime.interceptor;

import lombok.NonNull;

/**
 * {@link io.github.dddplus.ext.IDomainExtension}的方法拦截器.
 */
public interface IExtensionInterceptor {

    void beforeInvocation(@NonNull ExtensionContext context);

    void afterInvocation(@NonNull ExtensionContext context);
}
