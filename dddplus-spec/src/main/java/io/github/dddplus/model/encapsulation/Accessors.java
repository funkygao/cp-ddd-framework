/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.model.encapsulation;

import io.github.dddplus.model.IRepository;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 谁可以调用本方法.
 *
 * <p>例如，一个{@code Entity}为了能让{@link IRepository}在构造时传入一些属性，就需要把本来无需{@code public}的方法公开.</p>
 * <p>这破坏了封装，并且可能引发{@code bad side effect}.</p>
 * <p>为了解决这个问题，我们引入{@link Accessors}，并结合{@code AccessorsEnforcer}通过静态扫描保障.</p>
 */
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.SOURCE)
public @interface Accessors {

    /**
     * 哪些类可以访问本方法.
     *
     * <p>Example:</p>
     * <pre>
     * {@code
     *
     * public class Task {
     *     ℗Accessors(ITaskRepository.class)
     *     public setTaskItems(TaskItemBag taskItemBag) {}
     * }
     * }
     * </pre>
     */
    Class[] value();
}
