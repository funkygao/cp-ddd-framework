/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
/**
 * Reverse modeling DSL: Object-Oriented Domain Model(As-Is -> To-Be) Re-Design without tech constraints.
 * <p/>
 * <p>背景：好的业务模型不仅对产品架构、开发有重大意义，也能够快速的帮助客户分析管理上的不足、业务处理上的漏洞、不合理需求的悖论等.</p>
 * <p>目标：提供一套模型描述语言，从代码提炼精粹的业务知识，指导并约束模型和系统演进.</p>
 * <p>路径：明确业务中关键问题，跨越(架构约束，技术限制)等造成代码难以直观表达模型的问题，面向对象地抽取和修正，形成与正向模型正反馈闭环.</p>
 * <p>价值：通过逆向工程方法揭示代码实现里体现的核心领域问题，发现{@code essential problems}：一方面关联代码实现，一方面关联通用语言.</p>
 * <p/>
 * <p>《架构整洁之道》中的定义：软件架构是指设计软件的人为软件赋予的形状。</p>
 * <p>DSL标注过程，是无技术约束的OO二次设计的过程，是re-shape your code过程，是architecture discovery过程.</p>
 * <p>DSL标注过程，是Declarative Programming，而非Imperative Programming，关注描述问题和规则，而非实现步骤.</p>
 * <p>DSL标注过程，是对当前代码进行声明式编程的二次创作过程，它描述的问题的空间结构，结合版本控制就体现了时间维度.</p>
 * <p>DSL标注过程，是把领域知识注入代码的过程.</p>
 * <p>由于代码具有(可运行，包含完全细节，演进过程完整追溯，自我修复)特征，因此成为业务的唯一事实真相；但代码里有太多技术细节产生的业务模型噪音，导致代码里无法直观看到业务真相.</p>
 * <p>{@code 建模 = 图形 + 逻辑 + 现实的抽象}，代码(一维的，局部的)，而模型(多维立体的，全局的)，逆向模型相当于动态的活地图</p>
 * <p>通过该DSL建立的逆向模型，(业务强相关，代码强相关)，它完成了业务与代码双向映射，最终实现(业务模型，代码实现)的持续一致.</p>
 * <blockquote cite="Martin Fowler 1996 《Analysis Patterns: Reusable Object Models》">
 *     Although much of the attention in business engineering is about process, most of these patterns are static type models. I like to think of type models as defining the language of the business. These models thus provide a way of coming up with useful concepts that underlie a great deal of the process modeling.
 *     在面向对象开发过程中很重要的原则：要设计软件，使得软件的结构反映问题的结构。
 * </blockquote>
 * <pre>
 * DomainModel      -->   CodeImplementation
 *     ^                        |
 *     |                        V
 * KnowledgeCrunch  <--   ReversedDomainModel
 * </pre>
 * <p/>
 * <ul>DSL能力：
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
 * <li>显式标注，不标注则视为非模型要素</li>
 * <li>{@link io.github.dddplus.dsl.KeyElement#name()}，{@link io.github.dddplus.dsl.KeyBehavior#name()}等，修正关键概念名称</li>
 * <li>{@link io.github.dddplus.dsl.KeyFlow#actor()}，重新分配行为职责</li>
 * <li>{@link io.github.dddplus.dsl.IVirtualModel}，识别代码里缺失的关键职责对象，并在模型层建立</li>
 * </ul>
 * <p/>
 * <ul>如何评估模型质量？两大标准：1)拟合现状 2)预测未来
 * <li>模型是否直观完整表达出了业务核心知识</li>
 * <li>产品、业务方是否能看懂</li>
 * <li>日常开发，是否{@code 从模型中来，到模型中去}闭环</li>
 * </ul>
 * <ul>如何使用模型？模型驱动开发：
 * <li>业务需求 -> 从模型入手 -> 模型修改 -> 设计评审 -> 代码实现(依据活地图) -> 自动生成逆向模型，验证设计与实现一致性</li>
 * <li>围绕模型，持续构造易于维护的软件
 *   <ul>理解了模型
 *      <li>就大致理解了代码结构</li>
 *      <li>讨论需求时，研发就能容易明白需要改动的代码，评估工期和风险</li>
 *      <li>改功能就是改模型，改模型就等同于改代码，改代码就是改模型</li>
 *   </ul>
 * </li>
 * <li>使用该模型的典型场景：
 *   <ul>
 *       <li>我在网上看到一个开源ERP系统，如何快速掌握其核心设计？</li>
 *       <li>我是业务中台负责人，在与多条线BP合作扩展开发时，他们如何快速掌握我的设计，如何快速承接业务开发</li>
 *       <li>公司新找一批外包人员，如何让他们了解系统</li>
 *       <li>(业务方，产品，研发，架构)，他们统一的模型张什么样，它存在吗</li>
 *       <li>我是架构师，新进入一个团队，里面有十几个大型遗留系统，如何快速掌握它们，并识别核心问题</li>
 *   </ul>
 * </li>
 * </ul>
 * <ul>与{@code ArchiMate}/{@code BPMN}/{@code C4 Model}/{@code UML}/{@code TOGAF ADM}/{@code SysML}/{@code 4+1 View Model}等架构模型语言相比，本{@code DSL}：
 * <li>简单，门槛低</li>
 * <li>与代码强关联</li>
 * <li>具有修正机制</li>
 * </ul>
 * <ul>FAQ of the DSL：
 * <li>它在揭示整体结构上表现很好，但貌似缺乏要素间通信的表达力？
 *   <ul>
 *       <li>{@code DDDplus} is not going to re-invent the wheel</li>
 *       <li>{@code IDEA Sequence Diagram plugin}/BPMN等工具已经具备这类能力</li>
 *       <li>{@code CallGraphReport}会绘制关键方法的调用关系</li>
 *   </ul>
 * </li>
 * <li>业务约束/规则上貌似只是罗列，缺少场景表现力？
 *   <ul>
 *       <li>{@link io.github.dddplus.model.spcification.ISpecification}已经可以自动追溯了，因此没必要在逆向模型里体现，否则只会产生不必要噪音</li>
 *       <li>可视化的前提是：它真的可视</li>
 *   </ul>
 * </li>
 * <li>扩展能力如何可视化表达？
 *   <ul>
 *       <li>已经有了，please see {@code DomainArtifacts}</li>
 *   </ul>
 * </li>
 * <li>逆向模型貌似已经把代码变成结构化的数据了，我可以保存到数据库以产生下游应用吗？
 *   <ul>
 *       <li>Sure!</li>
 *       <li>please see {@code ReverseEngineeringModel}，which can be defined with multiple RDBMS tables</li>
 *   </ul>
 * </li>
 * </ul>
 *
 * @see <a href="https://xie.infoq.cn/article/3da89918c7d27ccc8e8f98ab7">面向对象设计的逆向建模方法和开源工具</a>
 * @see <a href="https://ieeexplore.ieee.org/document/723185/">Requirements for integrating software architecture and reengineering models</a>
 * @see <a href="http://www.jos.org.cn/jos/article/pdf/6278">面向领域驱动设计的逆向建模支持方法</a>
 * @see <a href="https://www.eclipse.org/MoDisco/">MoDisco</a>
 */
package io.github.dddplus.dsl;
