/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.model;

/**
 * 限定上下文下的领域模型.
 *
 * <p>具体场景相关，运营相关，承载对应场景下的(数据，规则，行为).</p>
 * <p>上下文范围内高内聚，为场景上下文服务</p>
 * <p>{@link BoundedDomainModel}是{@link IUnboundedDomainModel}的Proxy/Decorator.</p>
 */
public abstract class BoundedDomainModel<Model extends IUnboundedDomainModel> {
    protected final Model model;

    /**
     * 限定上下文模型的构造器.
     * <p>
     * <p>Visibility by design：这意味着{@link IUnboundedDomainModel}与{@link BoundedDomainModel}必须在同一个package.</p>
     *
     * @param model 基于哪一个非限定上下文模型
     */
    protected BoundedDomainModel(Model model) {
        this.model = model;
    }
}
