package org.example.bp.oms.isv.extension;

import org.example.bp.oms.isv.IsvPartner;
import org.example.cp.oms.spec.Steps;
import org.cdf.ddd.annotation.Extension;
import org.cdf.ddd.ext.IDecideStepsExt;
import org.cdf.ddd.model.IDomainModel;
import org.example.cp.oms.spec.resource.IStockService;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.*;

@Extension(code = IsvPartner.CODE, name = "ISV业务前台对所有流程的编排", value = "isvDecideStepsExt")
public class DecideStepsExt implements IDecideStepsExt {
    private static final List<String> emptySteps = Collections.emptyList();

    @Resource
    private IStockService stockService;

    @Override
    public List<String> decideSteps(@NotNull IDomainModel model, @NotNull String activityCode) {
        stockService.preOccupyStock("SKU From ISV"); // 测试Plugin的Spring注入功能

        List<String> steps = stepsRegistry.get(activityCode);
        if (steps == null) {
            return emptySteps;
        }

        return steps;
    }

    // 所有流程步骤注册表 {activityCode, stepCodeList}
    private static Map<String, List<String>> stepsRegistry = new HashMap<>();
    static {
        List<String> submitOrderSteps = new ArrayList<>();
        stepsRegistry.put(Steps.SubmitOrder.Activity, submitOrderSteps);
        submitOrderSteps.add(Steps.SubmitOrder.BasicStep);
        submitOrderSteps.add(Steps.SubmitOrder.PersistStep);
        submitOrderSteps.add(Steps.SubmitOrder.BroadcastStep);

        List<String> cancelOrderSteps = new ArrayList<>();
        stepsRegistry.put(Steps.CancelOrder.Activity, cancelOrderSteps);
        cancelOrderSteps.add(Steps.CancelOrder.BasicStep);
        cancelOrderSteps.add(Steps.CancelOrder.StateStep);
    }
}
