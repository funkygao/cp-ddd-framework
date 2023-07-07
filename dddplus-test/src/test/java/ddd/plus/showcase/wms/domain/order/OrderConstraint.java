package ddd.plus.showcase.wms.domain.order;

import io.github.dddplus.dsl.KeyRule;
import lombok.Data;

/**
 * 出库单生产约束，是业务规则.
 *
 * <p>生产计划负责生成.</p>
 */
@Data
public class OrderConstraint {
    /**
     * 生产流程是否配置为自动装箱.
     */
    private boolean autoCartonization;

    /**
     * 一单一包裹式作业模式.
     */
    private boolean useOnePack;

    /**
     * 是否采集信息：使用了哪些耗材.
     */
    private boolean collectConsumables;

    /**
     * 是否自动打包.
     */
    private boolean autoPack;

    /**
     * 自动发货，而无需人工操作：走数据流，与实物流分离
     */
    private boolean autoShip;

    @KeyRule
    public boolean allowManualCheck() {
        return !autoCartonization;
    }

}
