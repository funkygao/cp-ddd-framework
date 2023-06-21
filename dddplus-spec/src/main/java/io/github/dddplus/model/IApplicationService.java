/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.model;

/**
 * 应用层服务.
 *
 * <p>Application logic contains the workflow steps required to fulfill a business use case.</p>
 * <p>The application services don’t do any work, but they understand who to talk to to complete the task.</p>
 * <p>Coordinating the retrieval of domain objects from a data store, delegating work to them, and then saving the updated state is the responsibility of the application service layer.</p>
 */
public interface IApplicationService {
}
