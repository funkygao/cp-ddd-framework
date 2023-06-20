/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.model;

/**
 * 关联对象.
 *
 * @param <Entity> 被关联对象类型
 */
public interface HasOne<Entity> {

    Entity get();
}
