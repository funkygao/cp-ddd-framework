package org.example.bp.oms.isv.extension;

import lombok.extern.slf4j.Slf4j;
import org.cdf.ddd.annotation.Extension;
import org.cdf.ddd.api.ApiResult;
import org.cdf.ddd.api.RequestProfile;
import org.cdf.ddd.ext.IModelAttachmentExt;
import org.example.bp.oms.isv.IsvPartner;
import org.example.cp.oms.spec.model.IOrderModel;

import javax.validation.constraints.NotNull;

@Slf4j
@Extension(code = IsvPartner.CODE, value = "isvCustomModel", name = "ISV前台的订单个性化字段")
public class CustomModel implements IModelAttachmentExt<IOrderModel> {

    @Override
    public void explain(RequestProfile source, IOrderModel target) {

    }

    @Override
    public void explain(@NotNull IOrderModel model) {
    }

    @Override
    public void render(@NotNull IOrderModel source, @NotNull ApiResult target) {
    }
}
