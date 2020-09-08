package org.example.bp.oms.ka.extension;

import org.example.cp.oms.spec.partner.KaPartner;
import org.example.cp.oms.spec.Steps;
import org.ddd.cp.ddd.annotation.Extension;
import org.ddd.cp.ddd.ext.IDecideStepsExt;
import org.ddd.cp.ddd.model.IDomainModel;

import javax.validation.constraints.NotNull;
import java.util.*;

@Extension(code = KaPartner.CODE, name = "KA业务前台对所有流程的编排", value = "kaDecideStepsExt")
public class DecideStepsExt implements IDecideStepsExt {
    private static final List<String> emptySteps = Collections.emptyList();

    @Override
    public List<String> decideSteps(@NotNull IDomainModel model, @NotNull String activityCode) {
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

        List<String> cancelOrderSteps = new ArrayList<>();
        stepsRegistry.put(Steps.CancelOrder.Activity, cancelOrderSteps);
        cancelOrderSteps.add(Steps.CancelOrder.StateStep);
        cancelOrderSteps.add(Steps.CancelOrder.PersistStep);
    }
}
