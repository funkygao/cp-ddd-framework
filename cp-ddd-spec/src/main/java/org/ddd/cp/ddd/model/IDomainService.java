/*
 * Copyright cp-ddd-framework Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package org.ddd.cp.ddd.model;

/**
 * 领域服务.
 * <p>
 * <p>领域服务是主体，主体认识和改造客体({@code IDomainModel})</p>
 * <p>本框架内，领域服务根据粒度的粗细分为3层：</p>
 * <pre>
 *               +--------------------+
 *               |  BaseDomainAbility |
 *      +-----------------------------|
 *      |                 IDomainStep |
 * +----------------------------------|
 * |                          IDomain |
 * +----------------------------------+
 * </pre>
 */
public interface IDomainService {
}
