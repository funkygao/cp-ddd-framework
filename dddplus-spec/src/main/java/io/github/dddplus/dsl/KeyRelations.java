/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.dsl;

import java.lang.annotation.*;

/**
 * DO NOT use this annotation.
 *
 * <p>It exists just because of Java grammar constraints.</p>
 * @deprecated 不要直接使用.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface KeyRelations {
    KeyRelation[] value();
}
