/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
/**
 * Reverse modeling DSL: Object-Oriented Domain Model(As-Is -> To-Be) Re-Design without tech constraints.
 * <p/>
 * <p>由于代码具有(可运行，包含完全细节，演进过程完整追溯，自我修复)特征，因此成为业务的唯一事实真相；但代码里有太多技术细节产生的业务模型噪音，导致代码里无法直观看到业务真相.</p>
 * <p>{@code 建模 = 图形 + 逻辑 + 现实的抽象}，代码(一维的，局部的)，而模型(多维的，全局的)，逆向模型相当于动态的活地图</p>
 * <p>通过逆向建模建立全局认识的活地图，再去了解业务细节就会快得多。我们要建模的对象即是问题域，我们并不需要覆盖所有的点，而是基于核心的内容进行建模</p>
 * <p>通过该DSL建立的逆向模型，不仅仅是业务概念模型，更是一线研发可落地的系统模型，支持(正/逆)向循环迭代、交叉验证，最终实现(业务模型，代码)的一致.</p>
 * <pre>
 * DomainModel      -->   CodeImplementation
 *     ^                        |
 *     |                        V
 * KnowledgeCrunch  <--   ReversedDomainModel
 * </pre>
 * <p>DSL标注过程是二次抽象/二次设计/还原业务本质/揭示技术债的过程，不受任何技术约束限制，可以大胆使用OO思想，让service里的逻辑通过{@link io.github.dddplus.dsl.KeyFlow#actor()}回归到业务对象.</p>
 * <p>谁该来标注？最熟悉业务的一线研发！它体现了：(introspection, knowledge dissemination).</p>
 * <p/>
 * <ul>DSL标注结构：
 * <li>字段级：
 * <ul>
 *     <li>{@link io.github.dddplus.dsl.KeyElement}</li>
 * </ul>
 * </li>
 * <li>方法级：
 * <ul>
 *     <li>{@link io.github.dddplus.dsl.KeyRule}</li>
 *     <li>{@link io.github.dddplus.dsl.KeyBehavior}</li>
 *     <li>{@link io.github.dddplus.dsl.KeyFlow}</li>
 *     <li>{@link io.github.dddplus.dsl.KeyUsecase}</li>
 * </ul>
 * </li>
 * <li>类级：
 * <ul>
 *     <li>{@link io.github.dddplus.dsl.KeyRelation}</li>
 *     <li>{@link io.github.dddplus.dsl.KeyEvent}</li>
 *     <li>{@link io.github.dddplus.dsl.IVirtualModel}</li>
 * </ul>
 * </li>
 * <li>包级：
 * <ul>
 *     <li>{@link io.github.dddplus.dsl.Aggregate}</li>
 * </ul>
 * </li>
 * </ul>
 * <ul>DSL提供的代码模型修正机制：
 * <li>{@link io.github.dddplus.dsl.KeyElement#name()}，{@link io.github.dddplus.dsl.KeyBehavior#name()}等，修正关键概念名称</li>
 * <li>{@link io.github.dddplus.dsl.IVirtualModel}，识别关键职责对象</li>
 * <li>{@link io.github.dddplus.dsl.KeyFlow#actor()}，重新分配行为职责</li>
 * </ul>
 * <p/>
 * <ul>如何评估标注后生成的逆向模型质量？
 * <li>模型是否直观完整表达出了业务知识</li>
 * <li>日常开发，是否{@code 从模型中来，到模型中去}</li>
 * </ul>
 *
 * @see <a href="https://xie.infoq.cn/article/3da89918c7d27ccc8e8f98ab7">面向对象设计的逆向建模方法和开源工具</a>
 * @see <a href="https://ieeexplore.ieee.org/document/723185/">Requirements for integrating software architecture and reengineering models</a>
 * @see <a href="http://www.jos.org.cn/jos/article/pdf/6278">面向领域驱动设计的逆向建模支持方法</a>
 * @see <a href="https://www.eclipse.org/MoDisco/">MoDisco</a>
 */
package io.github.dddplus.dsl;
