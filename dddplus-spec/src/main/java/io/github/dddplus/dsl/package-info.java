/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
/**
 * Reverse modeling DSL.
 *
 * <p/>
 * <ol>为什么逆向建模：
 * <li>the architecture of an existing system is recovered from extracted source code artifacts</li>
 * <li>通过对比逆向领域模型与正向领域模型, 以快速发现二者的分歧(deviation)从而避免程序设计偏离领域模型</li>
 * <li>围绕抽象程序设计得到的领域模型展开知识消化, 减少其对代码实现细节的依赖, 从而加速知识消化过程</li>
 * </ol>
 * <pre>
 * DomainModel      -->   CodeImplementation
 *     ^                        |
 *     |                        V
 * KnowledgeCrunch  <--   ReversedDomainModel
 * </pre>
 * <ul>什么是领域模型：
 * <li>领域模型是领域业务逻辑的有组织且有选择的抽象</li>
 * <li>领域模型是由开发人员与领域专家协作构建出的一个反映深层次领域知识的模型，强调(业务，代码)始终统一</li>
 * <li>(领域模型 vs 代码实现)绑定，使得开发人员能够基于对模型的理解来解释和重构代码</li>
 * </ul>
 * <p/>
 * <ul>DSL的假设：
 * <li>(非一线研发，其他stakeholders)对模型的渴望程度是不同的</li>
 * <li>一线研发是(不理解DSL，可能遗漏标注DSL)的</li>
 * </ul>
 * <p></p>
 * <ul>DSL有效的前提：
 * <li>最少重复原则：否则代码变而标注不跟着改变，生成的领域知识产生误导</li>
 * <li>突出重点原则：主动放弃领域知识外实现细节，(清晰，完整)，高信噪比
 *  <ul>
 *      <li>领域意图不丢失原则</li>
 *  </ul>
 * </li>
 * <li>易于使用原则：一线研发轻松地在代码上打标，不漏用，不误用
 *  <ul>
 *      <li>漏用，可发现</li>
 *      <li>误用：可识别</li>
 *  </ul>
 * </li>
 * <li>唯一事实真相：代码特征(事实真相(哪怕它是错的)，有妥协，自我修复，完整追溯)
 *  <ul>
 *     <li>{@code DDD}是靠{@code Building Blocks}统一了(业务模型，代码)：代码直接映射业务概念，{@code DDDplus}补充了该{@code Building Blocks}</li>
 *     <li>代码级配置：(yaml, properties, dynamic config center)</li>
 *     <li>业务规则：{@link io.github.dddplus.model.IRule IRule}</li>
 *  </ul>
 * </li>
 * </ul>
 * <p/>
 * <ul>DSL目标：
 * <li>更高的抽象层级：避免一切技术细节，这样才能沟通(业务专家，开发人员，架构师)</li>
 * <li>隐性知识显性化：软件开发归根到底不是生产工程，而是知识工程</li>
 * <li>持续实现(领域模型，代码实现)的统一，追溯完整的模型演进过程</li>
 * <li>根据代码自动生成全部领域知识，业务方可理解的UL</li>
 * <li>自动揭露代码实现中存在的问题，(技术债，重构方向)，{@code DSL}可修复代码并揭露代码问题</li>
 * <li>(业务方，技术管理者，架构师，TL，不深谙其道的研发)实现细节无知的相关方，快速掌握全局和必要细节</li>
 * <li>业务开发，从模型中来到模型中去</li>
 * </ul>
 * <ul>结构化思维：
 * <li>以事物的结构为思考对象，来引导思维、表达和解决问题的一种思考方法</li>
 * <li>结构让思考问题更有逻辑、与人沟通更加清晰、解决问题更加高效</li>
 * <li>结构化思维的本质是信息熵：把知识的有序度最大化</li>
 * </ul>
 * <p/>
 * <ul>元模型标注，通过为代码附加元数据以帮助编译器理解程序结构：
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
 * <li>方法内部：WIP 通过注释</li>
 * <li>类级：
 * <ul>
 *     <li>{@link io.github.dddplus.dsl.KeyRelation}</li>
 *     <li>{@link io.github.dddplus.dsl.KeyEvent}</li>
 * </ul>
 * </li>
 * <li>包级：
 * <ul>
 *     <li>{@link io.github.dddplus.dsl.Aggregate}</li>
 * </ul>
 * </li>
 * </ul>
 * <p>如何评估标注后生成的逆向模型质量？</p>
 * <p>软件开发的核心难点在于处理隐藏在业务知识中的核心复杂度。因此，模型是否直观完整表达出了业务知识，是评判的标准</p>
 *
 * @see <a href="https://ieeexplore.ieee.org/document/723185/">Requirements for integrating software architecture and reengineering models</a>
 * @see <a href="http://www.jos.org.cn/jos/article/pdf/6278">面向领域驱动设计的逆向建模支持方法</a>
 * @see <a href="https://www.eclipse.org/MoDisco/">MoDisco</a>
 */
package io.github.dddplus.dsl;
