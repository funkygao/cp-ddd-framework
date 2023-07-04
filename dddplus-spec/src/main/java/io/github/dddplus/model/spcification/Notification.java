/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.model.spcification;

import java.util.ArrayList;
import java.util.List;

/**
 * A notification is an object that collects error messages.
 * <p>
 * <p>search {@code Notification Pattern & Specification Pattern} for more details</p>
 */
public class Notification {
    private List<String> errors;
    // each error has Object[] of args
    private List<Object[]> errorArgs;

    public static Notification build() {
        return new Notification();
    }

    private Notification() {}

    /**
     *
     * @param error
     * @param args message中的占位符参数值，for I18N
     */
    public void addError(String error, Object... args) {
        if (error == null || error.trim().isEmpty()) {
            // ignore empty error
            return;
        }

        if (errors == null) {
            errors = new ArrayList<>();
            errorArgs = new ArrayList<>();
        }

        errors.add(error);
        errorArgs.add(args);
    }

    public List<String> getErrors() {
        return errors;
    }

    public boolean hasError() {
        return errors != null && !errors.isEmpty();
    }

    public String first() {
        if (!hasError()) {
            return null;
        }

        return errors.get(0);
    }

    public Object[] firstArgs() {
        return errorArgs.get(0);
    }
}
