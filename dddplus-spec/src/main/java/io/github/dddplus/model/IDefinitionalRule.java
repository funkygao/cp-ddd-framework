/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.model;

/**
 * Definitional Rule, simply shapes knowledge about the world.
 *
 * <p>Definitional rules also shape people's behavior, but only indirectly.</p >
 * <p>Instead of behavior, they shape the concepts on which behavior is based.</p >
 * <ul>Key features:
 * <li>cannot be violated, ever</li>
 * <li>has inference rule</li>
 * </ul>
 * <p>示例：金卡会员允许进入库房参观。如果某金卡会员被禁止进入库房参观，一定不是因为金卡会员(概念性规则)被违背，而是行为性规则被破坏。而行为性规范被破坏，一定是有其他事实(fact)介入最终行为决策.</p >
 */
public interface IDefinitionalRule extends IRule {
}