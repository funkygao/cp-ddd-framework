/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.model;

/**
 * 脏数据提示.
 *
 * <p>它是聚合根改变状态时，由聚合根显式传递给{@code Repository}的待落库的脏数据携带者.</p>
 * <p>每个{@code IDirtyHint}包含了2层内容：</p>
 * <ul>
 * <li>业务语义，通过类名表达，{@code Repository}可以翻译成INSERT/UPDATE/DELETE数据库操作</li>
 * <li>携带的数据，{@code Repository}可以翻译成对应的表和字段</li>
 * </ul>
 * <p>业界有两个主流的变更追踪方案：</p>
 * <ul>
 * <li>基于snapshot方案：从数据库加载时内存保存一份snapshot，落库时(通过反射)与snapshot比较出变更的对象，全量diff</li>
 * <li>基于proxy方案：自动拦截setter方法，但需要框架支持，例如Entity Framework</li>
 * </ul>
 * <p>权衡下，我们使用了{@code IDirtyHint}方案，理解起来简单，性能有保障，风险低，也不依赖外部框架，但增加了开发成本</p>
 */
public interface IDirtyHint {
}
