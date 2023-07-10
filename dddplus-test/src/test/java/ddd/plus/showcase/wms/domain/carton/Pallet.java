package ddd.plus.showcase.wms.domain.carton;

import ddd.plus.showcase.wms.domain.task.TaskNo;
import io.github.dddplus.dsl.KeyBehavior;
import io.github.dddplus.dsl.KeyElement;

/**
 * 栈板，物流领域也称为托盘.
 */
public class Pallet {
    @KeyElement(types = KeyElement.Type.Structural, byType = true)
    private PalletNo palletNo;
    @KeyElement(types = KeyElement.Type.Structural, byType = true)
    private TaskNo taskNo;
    private Integer cartonTotal;

    @KeyBehavior
    void fulfill() {

    }

    void addCarton() {
        cartonTotal++;
    }
}
