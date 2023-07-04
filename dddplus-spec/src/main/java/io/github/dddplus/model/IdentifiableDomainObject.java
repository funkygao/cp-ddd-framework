/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.model;

import java.io.Serializable;

/**
 * Interface for domain objects that can be uniquely identified.
 *
 * @param <ID> the ID type.
 */
public interface IdentifiableDomainObject<ID extends Serializable> {

    /**
     * Returns the ID of this domain object.
     *
     * @return the ID or {@code null} if an ID has not been assigned yet.
     */
    ID getId();
}
