package ddd.plus.showcase.wms.domain.common;

import io.github.dddplus.dsl.KeyElement;
import io.github.dddplus.model.AbstractBusinessNo;
import lombok.Getter;
import lombok.NonNull;

import java.util.List;

/**
 * 货品.
 */
@Getter
public class Sku extends AbstractBusinessNo<String> {
    private String name;
    @KeyElement(types = KeyElement.Type.Structural, byType = true)
    protected Owner owner;
    @KeyElement(types = KeyElement.Type.Structural, byType = true)
    private PackCode packCode;
    @KeyElement(types = KeyElement.Type.Contextual, byType = true)
    private LotNo lotNo;
    @KeyElement(types = KeyElement.Type.Contextual)
    private UniqueCode uniqueCode;
    private Supplier supplier;
    private String brand;

    /**
     * 货品的序列号/serial number.
     *
     * <p>例如，iPhone，为了精确管理库存，每件货品都有一个唯一的序列号追踪.</p>
     * <p>该场景下，{@link Sku}代表的是{@code iPhone}，即“品”，而sn代表的是“件”.</p>
     */
    @KeyElement(types = KeyElement.Type.Contextual)
    private List<String> snList;

    protected Sku(@NonNull String skuNo) {
        super(skuNo);
    }

    public static Sku of(@NonNull String skuNo) {
        return new Sku(skuNo);
    }
}
