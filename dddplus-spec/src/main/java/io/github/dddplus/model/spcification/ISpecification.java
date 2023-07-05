/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.model.spcification;

/**
 * Specification interface，归约模式(Specification Pattern).
 * <p>
 * <p>{@link ISpecification} is part of our DDD building block.</p>
 * <p>Specifications are small, single‐purpose classes(SRP), similar to policies.</p>
 * <ul>不是Always-Valid Model吗？为什么还把规约提出来？不违反该原则吗？
 * <li>if validation depends exclusively on an entity's data then it stays inside the entity</li>
 * <li>if validation depends on information from multiple entities or external services, use Specification</li>
 * </ul>
 */
public interface ISpecification<T> {

    /**
     * Check if {@code candidate} is satisfied by the specification.
     *
     * @param candidate
     * @return
     */
    boolean isSatisfiedBy(T candidate);

    /**
     * Same as {@link #isSatisfiedBy(Object)} except that it accepts {@link Notification} to pass more detailed error info.
     *
     * @param candidate
     * @param notification
     * @return
     */
    boolean isSatisfiedBy(T candidate, Notification notification);
}
