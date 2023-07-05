package ddd.plus.showcase.wms.domain.flow;

import ddd.plus.showcase.wms.app.service.dto.RecommendPlatformRequest;
import ddd.plus.showcase.wms.domain.common.IMasterDataGateway;
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

    @KeyFlow
    public Platform execute(RecommendPlatformRequest request) {
        if (request.getOrderNo() != null) {
            return recommendByOrder(request);
        }

        return recommendByTaskBacklog(request);
    }

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

    private Platform recommendByOrder(RecommendPlatformRequest request) {
        OrderNo orderNo = OrderNo.of(request.getOrderNo());
        WarehouseNo warehouseNo = WarehouseNo.of(request.getWarehouseNo());
        Order order = orderRepository.mustGet(orderNo, warehouseNo);
        Platform platformNo = order.recommendedPlatformNo();
        if (platformNo.isPresent()) {
            // 这个订单之前已经推荐了，仍使用原来的复核台
            return platformNo;
        }

        TaskBag tasksOfOrder = taskRepository.tasksOfOrder(orderNo, warehouseNo);
        List<Platform> platformNos = tasksOfOrder.platformNos();
        if (!platformNos.isEmpty()) {
            return platformNos.get(0);
        }

        // 业务兜底
        return recommendByTaskBacklog(request);
    }

}
