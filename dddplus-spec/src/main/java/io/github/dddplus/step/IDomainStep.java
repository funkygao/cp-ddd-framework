/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.step;

import io.github.dddplus.model.IDomainModel;
import io.github.dddplus.model.IDomainService;
import lombok.NonNull;

/**
 * 领域活动(业务活动)的步骤，一种可以被编排的领域服务.
 * <p>
 * <p>一个领域活动(例如：接单是一个领域活动)是由多个步骤组成的.</p>
 * <p>步骤，相当于隐藏业务细节，宏观对业务活动的抽象.</p>
 * <p>
 * <p>普通的领域服务是业务系统主动声明接口并实现的，相当于主动提供服务.</p>
 * <p>而{@code IDomainStep}是框架层声明的接口，由业务系统在领域层实现，相当于被动提供服务.</p>
 *
 * @param <Model> 领域模型
 * @param <Ex>    中断步骤执行或改变后续步骤的异常
 */
@Deprecated
public interface IDomainStep<Model extends IDomainModel, Ex extends RuntimeException> extends IDomainService {

    /**
     * 执行本步骤.
     *
     * @param model 领域模型
     * @throws Ex 中断步骤执行或改变后续步骤的异常
     */
    void execute(@NonNull Model model) throws Ex;

    /**
     * 所属的领域活动编号.
     * <p>
     * <p>每一种领域活动，都有个唯一的编号</p>
     */
    @NonNull
    String activityCode();

    /**
     * 该步骤编号.
     */
    @NonNull
    String stepCode();
}
