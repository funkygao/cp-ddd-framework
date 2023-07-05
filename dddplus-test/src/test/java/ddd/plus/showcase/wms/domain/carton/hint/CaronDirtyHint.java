package ddd.plus.showcase.wms.domain.carton.hint;

import ddd.plus.showcase.wms.domain.carton.Carton;
import io.github.dddplus.model.IDirtyHint;
import io.github.dddplus.model.IMergeAwareDirtyHint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.BitSet;

@Getter
public class CaronDirtyHint implements IMergeAwareDirtyHint<Long> {
    @Getter(AccessLevel.PRIVATE)
    private BitSet dirtyMap = new BitSet(16);

    @AllArgsConstructor
    public enum Type {
        BindOrder(0),
        TransferFrom(1);
        int bit;
    }

    private final Carton carton;
    @Setter
    private BigDecimal checkedQty; // 冗余字段，该箱总计货品数量：为了便于数据库查询、排序

    public CaronDirtyHint(Carton carton, Type type) {
        this.carton = carton;
        this.dirtyMap.set(type.bit);
    }

    @Override
    public void onMerge(IDirtyHint thatHint) {
        CaronDirtyHint that = (CaronDirtyHint) thatHint;
        that.dirtyMap.or(this.dirtyMap);
        if (this.checkedQty != null) {
            that.checkedQty = this.checkedQty;
        }
    }

    @Override
    public Long getId() {
        return carton.getId();
    }
}
