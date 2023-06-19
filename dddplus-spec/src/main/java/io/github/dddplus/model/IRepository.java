/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.model;

/**
 * 仓库，负责聚合根的持久化，处理生命周期边界外的集合逻辑.
 *
 * <p>它只为聚合根服务，一个聚合只有一个{@link IRepository}，实现上一个仓库可以依赖多个{@code DAO}.</p>
 * <ul>注意事项：
 * <li>Do Create repositories for aggregate roots, not for all entities</li>
 * <li>Don’t Include Business Logic</li>
 * <li>Don’t Support Ad Hoc Queries findByXxx</li>
 * <li>Don’t Use Repositories for Reporting Needs</li>
 * </ul>
 */
public interface IRepository {
}
