/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.model.spcification;

/**
 * OR specification, used to create a new specification that is the OR of two other specifications.
 */
public class OrSpecification<T> extends AbstractSpecification<T> {

    private ISpecification<T> left;
    private ISpecification<T> right;

    /**
     * Create a new OR specification based on two other spec.
     *
     * @param left Specification one.
     * @param right Specification two.
     */
    public OrSpecification(final ISpecification<T> left, final ISpecification<T> right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean isSatisfiedBy(T candidate, Notification notification) {
        return left.isSatisfiedBy(candidate, notification) || right.isSatisfiedBy(candidate, notification);
    }
}
