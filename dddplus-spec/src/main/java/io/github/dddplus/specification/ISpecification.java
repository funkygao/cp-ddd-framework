/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.specification;

import javax.validation.constraints.NotNull;

/**
 * Specification declaration，是一种显式的业务规则，一种业务约束条件.
 * <p>
 * <p>通过{@link ISpecification}，可以把业务规则显性化，而不是散落在各处，便于复用.</p>
 * <p>同时，由于{@link ISpecification}的统一定义，也可以进行编排，统一处理.</p>
 * <p>{@link ISpecification}，is part of UL(Ubiquitous Language).</p>
 *
 * @param <T> The candidate business object.
 */
public interface ISpecification<T> {

    /**
     * Check whether a candidate business object satisfies the specification: the business rule.
     *
     * @param candidate The candidate business object
     * @return true if the business rule satisfied
     */
    default boolean isSatisfiedBy(@NotNull T candidate) {
        return isSatisfiedBy(candidate, null);
    }

    /**
     * Check whether a candidate business object satisfies the specification: the business rule.
     *
     * @param candidate    The candidate business object
     * @param notification Collect reasons why specification not satisfied. If null, will not collect unsatisfaction reasons.
     * @return true if the business rule satisfied
     */
    boolean isSatisfiedBy(@NotNull T candidate, Notification notification);
}
