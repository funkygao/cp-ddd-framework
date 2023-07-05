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
     * 该出库单是否人工复核.
     */
    @KeyRule
    public boolean manualCheck() {
        return !autoCartonization;
    }

}
