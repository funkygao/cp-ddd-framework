/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.model;

/**
 * 数据查询服务.
 *
 * <p>在DDD实践中，(查询，报表)类请求如果使用DDD Domain Layer得不偿失，还会聚合内部被迫暴露而破坏封装性.</p>
 * <p>因此我们采用应用层绕过领域层直接访问基础设施层的{@link IManager}实现这类请求.</p>
 */
public interface IManager extends IRepository {
}
