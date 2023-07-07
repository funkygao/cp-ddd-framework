package ddd.plus.showcase.wms.domain.carton.hint;

import ddd.plus.showcase.wms.domain.carton.Carton;
import ddd.plus.showcase.wms.domain.carton.Pallet;
import io.github.dddplus.model.IDirtyHint;
import io.github.dddplus.model.IMergeAwareDirtyHint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.BitSet;

@Getter
public class CartonDirtyHint implements IMergeAwareDirtyHint<Long> {
    private static int BITS = 8;

    @Getter(AccessLevel.PRIVATE)
    private final BitSet dirtyMap = new BitSet(BITS);

    @AllArgsConstructor
    public enum Type {
        BindOrder(1),
        FromContainer(2),
        Fulfill(3),
        InstallConsumables(4),
        PutOnPallet(5),
        ;
        int bit;

        BitSet dirtyMap() {
            BitSet s = new BitSet(BITS);
            s.set(bit);
            return s;
        }
    }

    private final Carton carton;

    @Setter
    private Pallet pallet;
    @Setter
    private BigDecimal checkedQty; // 冗余字段，该箱总计货品数量：为了便于数据库查询、排序

    public CartonDirtyHint(Carton carton, Type type) {
        this.carton = carton;
        this.dirtyMap.set(type.bit);
    }

    /**
     * 是否包括了指定类型.
     */
    public boolean has(Type type) {
        return dirtyMap.intersects(type.dirtyMap());
    }

    @Override
    public void onMerge(IDirtyHint thatHint) {
        CartonDirtyHint that = (CartonDirtyHint) thatHint;
        that.dirtyMap.or(this.dirtyMap);
        if (this.checkedQty != null) {
            that.checkedQty = this.checkedQty;
        }
        if (this.pallet != null) {
            that.pallet = this.pallet;
        }
    }

    @Override
    public Long getId() {
        return carton.getId();
    }
}
