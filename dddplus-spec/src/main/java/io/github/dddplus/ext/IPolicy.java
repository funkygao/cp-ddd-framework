/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ext;

import io.github.dddplus.model.IDomainService;
import lombok.NonNull;

/**
 * 单一扩展点的路由策略.
 *
 * <p>不同于 {@link IIdentityResolver} 需要{@code Router}类进行路由，{@link IPolicy}本身已经实现路由，无需定义{@code Router}类</p>
 * <p>{@link IPolicy}要实现在中台，这体现了"平台强管控"思想，场景的准入规则由中台控制，而不是BP自行控制</p>
 * <p>很多研发利用{@code Spring}机制，自行实现了路由策略：</p>
 * <pre>
 * {@code
 *
 * interface Policy {
 *     boolean support(Context context);
 *     void execute(Context context);
 * }
 *
 * List<Policy> policies; // 通过Spring自动注入
 * for (Policy policy : policies) {
 *     if (policy.support(context)) {
 *         policy.execute(context);
 *     }
 * }
 * }
 * </pre>
 * <p>这有什么问题？它耦合了(控制逻辑，执行逻辑)，丧失了平台强管控.</p>
 * <pre>
 * {@code
 *
 * ℗Policy
 * public class FetchWaybillGatewayPolicy implements IPolicy<IFetchWaybillGatewayExt, ShipmentOrder> {
 *     public static final String ABC = "abc"; // 供扩展点实现类绑定时引用
 *     public static final String Efg = "efg";
 *
 *     public String extensionCode(@NonNull ShipmentOrder identity) {
 *         ShipmentOrderPackContext packContext = identity.inContextOfPack();
 *         if (packContext.needWaybillFromAbc()) {
 *             return ABC;
 *         } else if (packContext.waybillFromEfg()) {
 *             return Efg;
 *         } else {
 *             return null; // 没有任何扩展点实现
 *         }
 *     }
 * }
 *
 * ℗Extension(code = FetchWaybillGatewayPolicy.ALPHA)
 * public class FetchWaybillGatewayExtAlpha implements IFetchWaybillGatewayExt {
 *     public List<Waybill> fetchWaybill(ShipmentOrder so, PackBag packBag) {
 *         return null;
 *     }
 * }
 *
 * // 预埋
 * List<Waybill> waybills = DDD.usePolicy(FetchWaybillGatewayPolicy.class, so).fetchWaybill(so, packBag);
 * if (waybills == null) {
 *     // 没有命中任何扩展点实现，这体现了扩展点的第1个语义：做不做
 * } else {
 *     // 有扩展点实现，并返回了数据，这体现了扩展点的第2个语义：怎么做
 * }
 * }
 * </pre>
 *
 * @param <Ext>      扩展点
 * @param <Identity> 业务身份
 */
public interface IPolicy<Ext extends IDomainExtension, Identity extends IIdentity> extends IDomainService {

    /**
     * 根据领域模型，定位匹配的扩展点.
     *
     * @param identity 业务身份
     * @return 匹配的扩展点编码，如果为空，则执行扩展点方法时返回null
     */
    String extensionCode(@NonNull Identity identity);
}
