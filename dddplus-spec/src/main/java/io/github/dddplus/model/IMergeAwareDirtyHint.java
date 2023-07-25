/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.model;

import java.io.Serializable;

/**
 * 可以合并的脏数据提示.
 *
 * <p>Example:</p>
 * <pre>
 * {@code
 *
 * public class OrderDirtyHint implements IMergeAwareDirtyHint<Long> {
 *     private static int BITS = 8;
 *     public enum Type {
 *         Type1(0),
 *         Type2(1),
 *         Type3(2);
 *         int bit;
 *
 *         BitSet dirtyMap() {
 *             BitSet s = new BitSet(BITS);
 *             s.set(bit);
 *             return s;
 *         }
 *     }
 *
 *     private final BitSet dirtyMap = new BitSet(BITS);
 *     private final Order order;
 *     private BigDecimal price;
 *
 *     public OrderDirtyHint(Order order, Type type) {
 *         this.order = order;
 *         this.dirtyMap.set(type.bit);
 *     }
 *
 *     public boolean has(Type type) {
 *         return dirtyMap.intersects(type.dirtyMap());
 *     }
 *
 *     public void onMerge(IDirtyHint thatHint) {
 *         OrderDirtyHint that = (OrderDirtyHint) thatHint;
 *         that.dirtyMap.or(dirtyMap);
 *         if (price != null) {
 *             that.price = price;
 *         }
 *     }
 *
 *     public Long getId() {
 *         return order.getId();
 *     }
 * }
 *
 * }
 * </pre>
 *
 * @param <ID> 该hint的唯一标识
 */
public interface IMergeAwareDirtyHint<ID extends Serializable> extends IDirtyHint, IdentifiableDomainObject<ID> {

    /**
     * Merge预留的hook.
     *
     * <p>注意：合并过程中要改变状态，要改变{@code thatHint}入参的状态，而不是改变{@code this}</p>
     *
     * @param thatHint {@link DirtyMemento}里现存的该hint
     */
    default void onMerge(IDirtyHint thatHint) {}
}
