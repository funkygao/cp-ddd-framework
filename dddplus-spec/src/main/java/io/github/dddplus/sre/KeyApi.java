/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.sre;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 核心API.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface KeyApi {

    /**
     * Top percentile 99 latency in ms.
     */
    int tp99();

    /**
     * Throughput upper limit in tps.
     */
    int throughput();

    /**
     * Availability in percentage.
     *
     * <p>e,g. 99.9 means 99.9%</p>
     */
    float availability();

}
