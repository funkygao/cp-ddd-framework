package org.example.bp.oms.ka.extension;

import lombok.extern.slf4j.Slf4j;
import org.example.bp.oms.ka.KaPartner;
import org.example.cp.oms.spec.ext.IAssignOrderNoExt;
import org.example.cp.oms.spec.model.IOrderModel;
import io.github.dddplus.annotation.Extension;
import org.example.cp.oms.spec.resource.IStockService;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

@Extension(code = KaPartner.CODE, value = "kaAssignOrderNoExt")
@Slf4j
public class AssignOrderNoExt implements IAssignOrderNoExt {
    public static final String KA_ORDER_NO = "KA1012";

    @Resource
    private IStockService stockService;

    @Override
    public void assignOrderNo(@NotNull IOrderModel model) {
        log.info("KA 预占库存 GSM098");
        if (!stockService.preOccupyStock("GSM098")) {
            throw new RuntimeException("预占库存失败");
        }

        model.assignOrderNo(this, KA_ORDER_NO);
    }
}
