package ddd.plus.showcase.wms.domain.common;

import io.github.dddplus.dsl.KeyElement;
import io.github.dddplus.model.AbstractBusinessNo;
import lombok.Getter;
import lombok.NonNull;

import java.util.List;

/**
 * 货品编号.
 */
@Getter
public class Sku extends AbstractBusinessNo<String> {
    private OwnerNo ownerNo;
    private PackCode packCode;
    @KeyElement(types = KeyElement.Type.Contextual)
    private LotNo lotNo;
    /**
     * 货品的序列号/serial number.
     *
     * <p>例如，iPhone，为了精确管理库存，每件货品都有一个唯一的序列号追踪.</p>
     * <p>该场景下，{@link Sku}代表的是{@code iPhone}，即“品”，而sn代表的是“件”.</p>
     */
    private List<String> snList;


    private Sku(@NonNull String skuNo) {
        super(skuNo);
    }

    public static Sku of(@NonNull String skuNo) {
        return new Sku(skuNo);
    }
}
