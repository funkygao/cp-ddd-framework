package ddd.plus.showcase.wms.domain.common.flow;

import ddd.plus.showcase.wms.app.service.dto.RecommendPlatformRequest;
import ddd.plus.showcase.wms.domain.common.gateway.IMasterDataGateway;
import ddd.plus.showcase.wms.domain.common.Platform;
import ddd.plus.showcase.wms.domain.common.WarehouseNo;
import ddd.plus.showcase.wms.domain.order.IOrderRepository;
import ddd.plus.showcase.wms.domain.order.Order;
import ddd.plus.showcase.wms.domain.order.OrderNo;
import ddd.plus.showcase.wms.domain.order.dict.OrderType;
import ddd.plus.showcase.wms.domain.task.ITaskRepository;
import ddd.plus.showcase.wms.domain.task.Task;
import ddd.plus.showcase.wms.domain.task.TaskBag;
import ddd.plus.showcase.wms.domain.task.dict.TaskMode;
import io.github.dddplus.dsl.KeyFlow;
import io.github.dddplus.model.INativeFlow;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@Setter(onMethod_ = {@Resource})
public class RecommendPlatformFlow implements INativeFlow {
    private ITaskRepository taskRepository;
    private IOrderRepository orderRepository;
    private IMasterDataGateway masterDataGateway;
    private Comparator<Platform> comparator;

    public Platform execute(RecommendPlatformRequest request) {
        if (request.getOrderNo() != null) {
            return recommendByOrder(request);
        }

        return recommendByTaskBacklog(request);
    }

    @KeyFlow(actor = TaskBag.class)
    private Platform recommendByTaskBacklog(RecommendPlatformRequest request) {
        List<Platform> platformNoList = masterDataGateway.candidatePlatforms(OrderType.valueOf(request.getOrderType()), TaskMode.valueOf(request.getTaskMode()), WarehouseNo.of(request.getWarehouseNo()));
        if (platformNoList.size() == 1) {
            return platformNoList.get(0);
        }

        // sort and find the best according to backlog
        Map<Platform, List<Task>> taskMap = taskRepository.pendingTasksOfPlatforms(platformNoList);
        platformNoList.forEach(platform -> {
            TaskBag taskBag = TaskBag.of(taskMap.get(platform));
            BigDecimal totalBacklogQty = taskBag.totalPendingQty();
            platform.setEffort(taskBag.totalCheckedQty());
            platform.setBacklog(totalBacklogQty);
        });

        platformNoList = new ArrayList<>(taskMap.keySet());
        platformNoList.stream().sorted(comparator);
        return platformNoList.get(0);
    }

    @KeyFlow(actor = Order.class)
    private Platform recommendByOrder(RecommendPlatformRequest request) {
        WarehouseNo warehouseNo = WarehouseNo.of(request.getWarehouseNo());
        Order order = orderRepository.mustGet(OrderNo.of(request.getOrderNo()), warehouseNo);
        Platform platformNo = order.recommendedPlatformNo();
        if (platformNo.isPresent()) {
            // 这个订单之前已经推荐了，仍使用原来的复核台
            return platformNo;
        }

        // TODO use association object
        TaskBag tasksOfOrder = taskRepository.tasksOfOrder(order.getOrderNo(), warehouseNo);
        List<Platform> platformNos = tasksOfOrder.platformNos();
        if (!platformNos.isEmpty()) {
            return platformNos.get(0);
        }

        // 业务兜底
        return recommendByTaskBacklog(request);
    }

}
