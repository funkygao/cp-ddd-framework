/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.runtime;

import io.github.dddplus.ext.IDomainExtension;
import io.github.dddplus.model.IDomainService;
import io.github.dddplus.ext.IIdentity;
import io.github.dddplus.runtime.registry.InternalIndexer;
import lombok.NonNull;

/**
 * 扩展点路由器的基础抽象类.
 * <p>
 * <p>{@code BaseRouter}是最小粒度的{@link IDomainService}，只负责一个扩展点的编排</p>
 *
 * @param <Ext>      扩展点
 * @param <Identity> 业务身份
 */
public abstract class BaseRouter<Ext extends IDomainExtension, Identity extends IIdentity> implements IDomainService {

    /**
     * 不关心扩展点返回值，逐一执行所有符合业务身份的扩展点.
     *
     * @param identity 业务身份
     * @return 扩展点，返回的是扩展点动态代理类，保证非null
     */
    protected Ext forEachExtension(@NonNull Identity identity) {
        return forEachExtension(identity, IReducer.allOf());
    }

    /**
     * 遍历满足条件的所有扩展点实例.
     * <p>
     * <p>通过{@link IReducer}控制何时退出遍历.
     *
     * @param identity 业务身份
     * @param reducer  收敛逻辑
     * @param <R>      扩展点方法的返回值类型
     * @return 扩展点，返回的是扩展点动态代理类，保证非null
     */
    protected <R> Ext forEachExtension(@NonNull Identity identity, @NonNull IReducer<R> reducer) {
        return forEachExtension(identity, 0, reducer);
    }

    /**
     * 遍历满足条件的所有扩展点实例，可指定超时条件.
     *
     * <p>通过{@link IReducer}控制何时退出遍历.</p>
     *
     * @param identity    业务身份
     * @param timeoutInMs 执行时的总超时条件
     * @param reducer     收敛逻辑
     * @param <R>         扩展点方法的返回值类型
     * @return 扩展点，返回的是扩展点动态代理类，保证非null
     */
    private <R> Ext forEachExtension(@NonNull Identity identity, int timeoutInMs, @NonNull IReducer<R> reducer) {
        Class<? extends IDomainExtension> extClazz = InternalIndexer.getBaseRouterExtDeclaration(this.getClass());
        return findExtension((Class<Ext>) extClazz, identity, reducer, defaultExtension(identity), timeoutInMs);
    }

    /**
     * 找到第一个符合条件的扩展点实例.
     * <p>
     * <p>这表示：扩展点实例之间是互斥的，无法叠加的</p>
     * <p>如果需要根据扩展点执行结果来找第一个匹配的扩展点实例，请使用{@link #forEachExtension(Identity, IReducer)}</p>
     *
     * @param identity 业务身份
     * @return 扩展点，返回的是扩展点动态代理类，保证非null
     */
    protected Ext firstExtension(@NonNull Identity identity) {
        return firstExtension(identity, 0);
    }

    /**
     * 找到第一个符合条件的扩展点实例，并指定扩展点最大执行时长，超时抛出{@link java.util.concurrent.TimeoutException}.
     * <p>
     * <p>这表示：扩展点实例之间是互斥的，无法叠加的</p>
     * <p>如果需要根据扩展点执行结果来找第一个匹配的扩展点实例，请使用{@link #forEachExtension(Identity, IReducer)}</p>
     *
     * @param identity    业务身份
     * @param timeoutInMs 执行扩展点的超时时间，in ms；如果超时，会强行终止扩展点的执行
     * @return 扩展点，返回的是扩展点动态代理类，保证非null
     */
    protected Ext firstExtension(@NonNull Identity identity, int timeoutInMs) {
        Class<? extends IDomainExtension> extClazz = InternalIndexer.getBaseRouterExtDeclaration(this.getClass());
        return findExtension((Class<Ext>) extClazz, identity, null, defaultExtension(identity), timeoutInMs);
    }

    /**
     * 默认扩展点实现.
     *
     * <p>运行时，只有没有找到任何1个扩展点实现时，才会执行默认扩展点实现</p>
     *
     * @param identity 业务身份
     * @return 如果为null，而且触发了默认扩展点执行，扩展点方法永远返回null。这要求我们：扩展点方法的返回值不能是int/boolean等，否则会抛出NPE!
     */
    public abstract Ext defaultExtension(@NonNull Identity identity);

    private <Ext extends IDomainExtension, R> Ext findExtension(@NonNull Class<Ext> extClazz, @NonNull Identity identity, IReducer<R> reducer, Ext defaultExt, int timeoutInMs) {
        ExtensionInvocationHandler<Ext, R> proxy = new ExtensionInvocationHandler<>(extClazz, identity, reducer, defaultExt, InternalIndexer.registeredInterceptor(), timeoutInMs);
        return proxy.createProxy();
    }
}
