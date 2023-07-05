/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.model.spcification;

/**
 * AND specification, used to create a new specification that is the AND of two other specifications.
 */
public class AndSpecification<T> extends AbstractSpecification<T> {

    private ISpecification<T> left;
    private ISpecification<T> right;

    /**
     * Create a new AND specification based on two other spec.
     *
     * @param left Specification one.
     * @param right Specification two.
     */
    public AndSpecification(final ISpecification<T> left, final ISpecification<T> right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean isSatisfiedBy(T candidate, Notification notification) {
        return left.isSatisfiedBy(candidate, notification) && right.isSatisfiedBy(candidate, notification);
    }
}
