/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
/**
 * DDD patched building blocks.
 *
 * <p>软件开发的核心难点，就是处理隐藏在业务知识中的核心复杂度，除了清晰地理解业务诉求之外，还需要通过建模的方式对这种复杂度进行简化与精炼.</p>
 * <p>数据量大导致性能慢、高并发等问题，不是核心复杂性，只是阶段性问题.</p>
 * <p>DDD is a set of guidelines, not rules. Do whatever makes sense in your situation, don't just follow DDD blindly.</p>
 * <p>领域是客观的，领域模型是主观的，体现了程序员对领域的认识.</p>
 * <p>这要求我们业务开发从离散的功能建设，转换到统一的模型/能力建设.</p>
 * <p>DDD says we don’t just want our systems to work, we want to truly understand what we’re building.</p>
 * <p>What DDD emphasizes is a deep understanding of the problem domain, not just an understanding of the solution.</p>
 * <p>Why Anaemic Domains harm our projects? They mix the “what” and the “how”, violate concepts of decoupling and separation of concern.</p>
 * <p>此外，贫血模型将“共性”统统视为“个性”，这是抹掉“共性”行为的做法，与“充血模型”抹掉“个性”的做法刚好相反，但它们都没有协调好“共性”与“个性”的关系.</p>
 * <ol>行为，有2类：
 * <li>职责：对象内部行为</li>
 * <li>协作：对象之间行为</li>
 * </ol>
 * <p>领域模型是反映深层次领域知识的模型，它对领域的业务逻辑进行了有组织、有选择的抽象，使得领域知识自然转化为软件开发的最终产品.</p>
 * <p>DDD的模型本质上是领域知识模型，功能是表象，模型才是内在。好的模型可以改善我们的认知，降低复杂性，提升对于变更的适应性，提升演进能力.</p>
 * <p>领域知识是指在特定领域内所需要的知识和技能，具体包括了：领域背景(历史，发展，趋势，文化，社区)，领域业务(业务流程/流程间依赖关系，业务对象、对象间关系、业务规则、应用场景等)，领域技术，领域人员.</p>
 * <p>业务规则是对企业或组织业务过程的约束条件，它描述了业务流程的各种限制、规范和条件。业务规则通常是由法律、政策、行业标准、企业内部规章制度等产生的，可以用来约束业务流程的各种行为和决策.</p>
 * <ol>业务规则通常包括以下几种类型：
 * <li>约束性规则：指必须遵守的规则，如法律、政策等规定</li>
 * <li>规范性规则：指应该遵守的规则，如行业标准、企业内部规章制度等</li>
 * <li>推荐性规则：指建议性规则，如最佳实践、经验分享等</li>
 * <li>条件性规则：指在一定条件下才适用的规则，如优惠活动、特殊情况等</li>
 * </ol>
 * <ul>形而下和形而上是哲学上的两个概念，用于描述世界和事物的本质和形式
 * <li>形而下指具体的、物质的、感性的世界，即我们所能感知到的世界，是一种低级的、表面的、局部的存在</li>
 * <li>形而上指抽象的、超越感性的、理性的世界，即我们所不能感知到的世界，是一种高级的、本质的、普遍的存在</li>
 * </ul>
 * <p>抽象，是指从具体的事物中主动抛弃非核心细节从而提取出共性的概念或特征，形成一个更为一般化的简化表示，同时向程序员提供最相关的信息。做抽象需要善于归纳和总结，辨别共性和差异.</p>
 * <ul>Insight
 * <li>Technical solutions built for specific business problems are not reusable in a fluid business landscape</li>
 * <li>We encounter different behaviours for the same entity more often than we encounter new entities</li>
 * <li>Since platform service are unaware of the products built on top of them, they should ideally not have code to invoke those components directly</li>
 * <li>Changes to business will either result in ever increasing “if-else” or recurring rewrites. It is difficult to change behaviour of the system without touching core codebase</li>
 * </ul>
 * @see <a href="https://enterprisecraftsmanship.com/posts/domain-model-purity-completeness/">DDD Entity Trilemma</a>
 */
package io.github.dddplus.model;
