package ddd.plus.showcase.wms.domain.common;

import io.github.dddplus.model.AbstractBusinessNo;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 复核台(操作台).
 *
 * <p>对于一个规模大的仓库，复核台数量可能300+</p>
 */
public class Platform extends AbstractBusinessNo<String> {
    private Platform(String value) {
        super(value);
    }

    public static Platform of(String platformNo) {
        return new Platform(platformNo);
    }

    /**
     * 积压量
     */
    @Getter
    @Setter
    private BigDecimal backlog = BigDecimal.ZERO;

    /**
     * 工作量，即已经复核了多少货量.
     */
    @Getter
    @Setter
    private BigDecimal finishedWorkload = BigDecimal.ZERO;
}
