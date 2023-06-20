package io.github.dddplus.model;

import java.util.List;

/**
 * 基于内存的{@link HasMany}实现.
 *
 * @param <Entity> 实体类型
 */
public class Bag<Entity> implements HasMany<Entity> {
    private List<Entity> list;

}
