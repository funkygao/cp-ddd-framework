package com.liteflow;

import com.liteflow.components.packflow.context.PackFlowContext;
import com.liteflow.components.packflow.domain.CartonBag;
import com.liteflow.components.packflow.domain.PlatformNo;
import com.liteflow.components.packflow.domain.ShipmentOrder;
import com.yomahub.liteflow.core.FlowExecutor;
import com.yomahub.liteflow.core.FlowExecutorHolder;
import com.yomahub.liteflow.flow.LiteflowResponse;
import com.yomahub.liteflow.flow.entity.CmpStep;
import com.yomahub.liteflow.property.LiteflowConfig;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class PackFlowTest {

    void createPackWithCartonBag(ShipmentOrder so, Integer packageQty, CartonBag cartonBag, PlatformNo platformNo) {
        LiteflowConfig config = new LiteflowConfig();
        config.setRuleSource("liteflow/**/*.el.xml");
        // 不建议每次执行流程都去初始化FlowExecutor，这个对象的初始化工作相对比较重
        FlowExecutor flowExecutor = FlowExecutorHolder.loadInstance(config);

        PackFlowContext context = new PackFlowContext(so, cartonBag, platformNo);
        LiteflowResponse response = flowExecutor.execute2Resp("PackFlow", packageQty, context);
        if (response.isSuccess()) {
            Map<String, CmpStep> steps = response.getExecuteSteps();
            log.info("actual steps: {}", steps);
            context = response.getFirstContextBean(); // 上下文对象最新值
        } else {
            Exception cause = response.getCause();
            cause.printStackTrace();
        }

    }
}
