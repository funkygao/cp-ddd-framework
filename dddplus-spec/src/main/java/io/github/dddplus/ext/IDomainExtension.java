/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ext;

/**
 * 扩展点：业务语义/意图确定，但具体场景内部逻辑不同的业务功能点。业务上，代表业务变化点，体现场景差异.
 * <p>
 * <p>软件是变与不变交融的艺术，变化带来发展，不变的是本质；透过变化抓住不变，才是设计的根本.</p>
 * <p>扩展点，将原来静态的逻辑转为动态的逻辑，分离了(变化，不变)，不变的是意图，变化的是在不同场景下如何达成意图.</p>
 * <p>通过扩展点这个表达业务意图(WHAT, not HOW)的接口，实现业务功能点的多态.</p>
 * <ul>扩展点的预埋(对扩展点接口的调用)，可以表达出两个含义：
 * <li>做不做，maybe do，有的场景可能不做</li>
 * <li>怎么做，how to do，做的场景间有差异</li>
 * </ul>
 * <p>!!! ATTENTION !!!</p>
 * <p>扩展点方法的返回值必须是Java包装类或void，不能是int/boolean等primitive types，否则可能抛出NPE!</p>
 */
public interface IDomainExtension extends IPlugable {

    /**
     * 推荐使用：扩展点的默认实现对应的码，这样你就不用再定义了.
     *
     * <p>不同扩展点的默认码是可以相同的，而同一个扩展点的码不能重复</p>
     */
    String DefaultCode = "_default__";
}
