/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.model;

/**
 * 应用层服务.
 *
 * <p>远离业务模型核心层.</p>
 * <p>应用层，负责组织业务场景，编排业务，隔离场景对领域层的差异；领域层，实现具体的业务逻辑、规则，为应用层提供无差别的服务能力.</p>
 * <p>如果没有应用层，会导致领域层与具体场景绑定，复用性大大降低.</p>
 * <p>Application logic contains the workflow steps required to fulfill a business use case.</p>
 * <p>The application services don’t do any work, but they understand who to talk to to complete the task.</p>
 * <p>Coordinating the retrieval of domain objects from a data store, delegating work to them, and then saving the updated state is the responsibility of the application service layer.</p>
 */
public interface IApplicationService {
}
