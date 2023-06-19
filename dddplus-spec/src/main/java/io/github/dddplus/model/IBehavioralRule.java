/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.model;

/**
 * Behavioral Rule, aim to directly shape people's behavior.
 *
 * <p>They set boundaries for acceptable behavior.</p >
 * <ul>Key features:
 * <li>can be violated: 警察局，审判机构，监狱，都是为它可能被破坏而准备的</li>
 * <li>confer rights in groups and communities of people</li>
 * </ul>
 * <p>行为的：指导，约束条件，执行规范</p >
 * <ul>行为性规则，通常表现为如下形式：
 * <li>must or must not be</li>
 * <li>should or should not be</li>
 * <li>how to be</li>
 * </ul>
 * <p>Behavioral rules without {@link IDefinitionalRule} are often vague. Lack of precision means enforcement will be inconsistent or capricious.</p >
 * <p>行为性规则，有可以类比的两种类型：{@code hard limit} vs {@code soft limit}.</p >
 * <ul>行为性规则的实际例子：
 * <li>An order over $1,000 must not be accepted on credit without a credit check</li>
 * <li>An order's date promised must be at least 24 hours after the order's date taken</li>
 * </ul>
 */
public interface IBehavioralRule extends IRule {
}