/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ext;

/**
 * 扩展点：业务语义/意图确定，但具体场景内部逻辑不同的业务功能点。业务上，代表业务变化点，体现场景差异.
 * <p>
 * <p>软件是变与不变交融的艺术，变化带来发展，不变是本质；透过变化抓住不变，才是设计的根本.</p>
 * <p>扩展点，将原来静态的逻辑转为动态的逻辑，分离了(变化，不变)，不变的是意图，变化的是在不同场景下如何达成意图.</p>
 * <p>通过扩展点这个表达业务意图(WHAT, not HOW)的接口，实现业务功能点的多态，解耦不同的变化，隔离变化.</p>
 * <p>定义扩展点接口，实际上是在做标准化，通过概念上剥离实现屏蔽(变化，细节)，这是分层思想.</p>
 * <p>扩展点的本质是抽象，抽象的本质是识别问题本质，途径是主动忽略非本质细节.</p>
 * <ul>扩展点的预埋(对扩展点接口的调用)，可以表达出2个语义：
 * <li>做不做，maybe do，有的场景可能不做</li>
 * <li>怎么做，how to do，做的场景间有差异</li>
 * </ul>
 * <p>对于第1个语义，预埋扩展点处要判断返回值是否null，null表示没有命中任何扩展点实现</p>
 * <p>如果觉得判空不优雅，也可以提供扩展点的兜底实现，这样预埋处就不必判空了</p>
 * <p>!!! ATTENTION !!!</p>
 * <p>扩展点方法的返回值必须是Java包装类或void，不能是int/boolean等primitive types，否则可能抛出NPE!</p>
 * <p>可以使用{@code ExtensionMethodNPEEnforcer}自动检测，避免线上风险</p>
 * <pre>
 * {@code
 *
 * class Test {
 *     ℗Test
 *     void enforceExtensionMethodSignature() {
 *         new ExtensionMethodSignatureEnforcer()
 *                 .scan(root)
 *                 .enforce();
 *     }
 * }
 * }
 * </pre>
 * <p>扩展点方法的入参，有时候平台传递的是像{@code Order}这样的大对象，但又担心BP擅自调用大对象的写方法产生{@code unexpected side effect}，可以这样：</p>
 * <pre>
 * {@code
 *
 * class Order implements IOrder {
 *     private String orderNo;
 *     private List<OrderLine> orderLines;
 * }
 * interface IOrder {
 *     List<? extends IOrderLine> getOrderLines();
 * }
 *
 * class OrderLine implements IOrderLine {
 *     private String sku;
 *     private BigDecimal qty;
 * }
 * interface IOrderLine {
 *     String getSku();
 *     BigDecimal getQty();
 * }
 *
 * // 扩展点预埋
 * Order order = factory.create();
 * DDD.usePolicy(SomePolicy.class, ident).doSth(order);
 *
 * // 扩展点实现
 * class DoSthExt implements IDoSthExt {
 *     public void doSth(IOrder order) { // 扩展点方法暴露的是抽象的接口
 *         // 只能根据平台的定义进行有限访问Order功能
 *     }
 * }
 * }
 * </pre>
 */
public interface IDomainExtension extends IPlugable {

    /**
     * 推荐使用：扩展点的默认实现对应的码，这样你就不用再定义了.
     *
     * <p>不同扩展点的默认码是可以相同的，而同一个扩展点的码不能重复</p>
     */
    String DefaultCode = "_default__";
}
