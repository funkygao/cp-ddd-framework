/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.model.spcification;

/**
 * Abstract base implementation of composite {@link ISpecification} with default
 * implementations for {@code and}, {@code or}.
 */
public abstract class AbstractSpecification<T> implements ISpecification<T> {
    
    @Override
    public final boolean isSatisfiedBy(T candidate) {
        return isSatisfiedBy(candidate, Notification.build());
    }

    public AbstractSpecification<T> and(final ISpecification<T> specification) {
        return new AndSpecification<T>(this, specification);
    }

    public AbstractSpecification<T> or(final ISpecification<T> specification) {
        return new OrSpecification<T>(this, specification);
    }
}