package io.github.dddplus.runtime.interceptor;

import io.github.dddplus.annotation.Extension;
import io.github.dddplus.ext.IDomainExtension;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.Method;

/**
 * 当前扩展点运行时上下文.
 *
 * <p>供{@link IExtensionInterceptor}使用.</p>
 */
@AllArgsConstructor
@Getter
public class ExtensionContext {
    /**
     * {@link Extension#code()}.
     */
    private final String code;

    /**
     * 当前扩展点实例.
     */
    private final IDomainExtension extension;

    /**
     * 当前扩展点执行哪一个方法.
     */
    private final Method method;

    /**
     * 当前扩展点方法的入参.
     */
    private final Object[] args;
}
