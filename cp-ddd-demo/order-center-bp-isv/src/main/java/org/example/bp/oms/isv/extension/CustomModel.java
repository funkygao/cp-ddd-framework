package org.example.bp.oms.isv.extension;

import lombok.extern.slf4j.Slf4j;
import org.cdf.ddd.annotation.Extension;
import org.cdf.ddd.api.ApiResult;
import org.cdf.ddd.api.RequestProfile;
import org.cdf.ddd.ext.IModelAttachmentExt;
import org.example.bp.oms.isv.IsvPartner;
import org.example.cp.oms.spec.exception.OrderErrorReason;
import org.example.cp.oms.spec.exception.OrderException;
import org.example.cp.oms.spec.model.IOrderModel;

import javax.validation.constraints.NotNull;
import java.util.Map;

@Slf4j
@Extension(code = IsvPartner.CODE, value = "isvCustomModel", name = "ISV前台的订单个性化字段")
public class CustomModel implements IModelAttachmentExt<IOrderModel> {
    private static final String KEY_STATION_NO = "_station_contact_";

    @Override
    public void explain(@NotNull RequestProfile source, @NotNull IOrderModel target) {
        // 入参里预留了扩展属性
        Map<String, String> ext = source.getExt();
        // 站点联系人号码，是个性化字段，中台只存储，不负责逻辑：前台来处理逻辑，并告诉中台存储到哪些已预留的字段
        String stationContactNo = ext.get(KEY_STATION_NO);
        if (stationContactNo == null || stationContactNo.length() < 5) {
            // ISV针对该字段的特有业务逻辑
            throw new OrderException(OrderErrorReason.Custom.Custom).withCustom("109");
        }

        // 落到预留字段上，ISV想把它保存到x2字段
        // 注意：预留字段也可能保存复杂对象，例如json：前台进行codec就好
        log.info("站点联系人号码：{}，保存到x2字段", stationContactNo);
        target.setX2(stationContactNo);
    }

    @Override
    public void explain(@NotNull IOrderModel model) {
    }

    @Override
    public void render(@NotNull IOrderModel source, @NotNull ApiResult target) {
    }
}
