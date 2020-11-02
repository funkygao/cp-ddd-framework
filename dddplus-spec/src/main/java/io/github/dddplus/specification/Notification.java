/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.specification;

import java.util.ArrayList;
import java.util.List;

/**
 * A notification is an object that collects reason why specifications not satisfied.
 * <p>
 * <p>google Notification Pattern & Specification Pattern for more details</p>
 */
public class Notification {

    // reasons why specification not satisfied
    private List<String> reasons;

    /**
     * Create an empty {@code Notification} instance.
     */
    public static Notification create() {
        return new Notification();
    }

    private Notification() {
        this.reasons = new ArrayList<>();
    }

    /**
     * Add a reason why this specification not satisfied.
     * <p>
     * <p>Will filter out duplicated reason under the hood.</p>
     *
     * @param reason unsatisfaction reason. if null, will be ignored and return false
     * @return true if this {@code Notification} changed as a result of the call
     */
    public boolean addReason(String reason) {
        if (reason == null || reason.trim().isEmpty()) {
            // ignore empty reason
            return false;
        }

        // duplication check
        for (String r : reasons) {
            if (r.equals(reason)) {
                return false;
            }
        }

        return reasons.add(reason);
    }

    /**
     * Returns true if this {@code Notification} contains no unsatisfaction reasons.
     */
    public boolean isEmpty() {
        return reasons.isEmpty();
    }

    /**
     * Returns the number of unsatisfaction reasons.
     */
    public int size() {
        return reasons.size();
    }

    /**
     * Return the collected unsatisfaction reasons.
     */
    public List<String> reasons() {
        return reasons;
    }

    /**
     * Get the first unsatisfaction reason if any.
     *
     * @return null if empty
     */
    public String firstReason() {
        if (isEmpty()) {
            return null;
        }

        return reasons.get(0);
    }
}
