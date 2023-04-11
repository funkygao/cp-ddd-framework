/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ext;

import io.github.dddplus.model.IDomainService;
import io.github.dddplus.model.IIdentity;
import lombok.NonNull;

/**
 * 单一扩展点的动态路由策略.
 *
 * <p>不同于 {@link IIdentityResolver} 的静态绑定，扩展点定位策略是动态的.</p>
 * <p>每一个扩展点定位策略只能有一个实例，并且绑定到一个扩展点，此外也不再需要额外的扩展点路由类.</p>
 * <p>每个实现{@link IPolicy}的类，必须使用{@code Policy}注解进行标注!</p>
 * <p>最佳实践：Policy code，定义在{@link IPolicy}的实现类里，供扩展点实现者使用：</p>
 * <pre>
 * {@code
 * @Policy(extClazz = IFetchWaybillGatewayExt.class)
 * public class FetchWaybillGatewayPolicy implements IPolicy<ShipmentOrder> {
 *     public static final String NETEASE = "网易";
 *     public static final String ALPHA = "alpha";
 *     public static final String UNKNOWN = "不支持";
 *
 *     @Override
 *     public @NonNull String extensionCode(@NonNull ShipmentOrder identity) {
 *         ShipmentOrderPackContext packContext = identity.inContextOfPack();
 *         if (packContext.needWaybillFromNetease()) {
 *             return NETEASE;
 *         } else if (packContext.waybillFromAlpha()) {
 *             return ALPHA;
 *         } else {
 *             return UNKNOWN;
 *         }
 *     }
 * }
 *
 * @Extension(code = FetchWaybillGatewayPolicy.ALPHA)
 * public class FetchWaybillGatewayExtAlpha implements IFetchWaybillGatewayExt {
 *     @Override
 *     public List<Waybill> fetchWaybill(ShipmentOrder so, PackBag packBag) {
 *         return null;
 *     }
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
