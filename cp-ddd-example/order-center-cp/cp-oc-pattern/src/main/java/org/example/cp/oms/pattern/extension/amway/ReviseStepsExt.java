package org.example.cp.oms.pattern.extension.amway;

import org.x.cp.ddd.annotation.Extension;
import org.example.cp.oms.pattern.AmwayPattern;
import org.example.cp.oms.spec.Steps;
import org.example.cp.oms.spec.ext.IReviseStepsExt;
import org.example.cp.oms.spec.model.IOrderModel;

import java.util.ArrayList;
import java.util.List;

@Extension(code = AmwayPattern.CODE, value = "amwayReviseStepsExt")
public class ReviseStepsExt implements IReviseStepsExt {

    @Override
    public List<String> reviseSteps(IOrderModel model) {
        if (Steps.SubmitOrder.Activity.equals(model.currentActivity())) {
            if (model.currentStep().equals(Steps.SubmitOrder.BasicStep)) {
                List<String> subsequentSteps = new ArrayList<>();
                return subsequentSteps; // 没有后续步骤了：跳过PersistStep
            }
        }

        List<String> result = new ArrayList<>();
        return result;
    }
}
