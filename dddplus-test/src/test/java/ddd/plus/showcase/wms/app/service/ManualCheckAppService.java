package ddd.plus.showcase.wms.app.service;

import ddd.plus.showcase.wms.app.UnitOfWork;
import ddd.plus.showcase.wms.app.service.dto.ClaimTaskRequest;
import ddd.plus.showcase.wms.app.service.dto.ConfirmQtyRequest;
import ddd.plus.showcase.wms.app.service.dto.RecommendPlatformRequest;
import ddd.plus.showcase.wms.app.service.dto.base.ApiResponse;
import ddd.plus.showcase.wms.domain.common.*;
import ddd.plus.showcase.wms.domain.order.OrderNo;
import ddd.plus.showcase.wms.domain.task.PlatformNo;
import ddd.plus.showcase.wms.domain.task.Task;
import ddd.plus.showcase.wms.domain.task.TaskNo;
import ddd.plus.showcase.wms.domain.task.ITaskRepository;
import ddd.plus.showcase.wms.domain.task.spec.OperatorCannotBePicker;
import ddd.plus.showcase.wms.domain.task.spec.TaskCanPerformChecking;
import io.github.dddplus.dsl.KeyUsecase;
import io.github.design.ContainerNo;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * 人工复核用例.
 */
@Service
@Setter(onMethod_ = {@Resource})
@Slf4j
public class ManualCheckAppService {
    private MasterDataGateway masterDataGateway;
    private ITaskRepository taskRepository;
    private UnitOfWork uow;

    /**
     * 操作员在执行一个复核任务，他应该去哪一个复核台?
     */
    @KeyUsecase(in = "taskNo", out = "platformNo")
    public ApiResponse<String> recommendPlatform(RecommendPlatformRequest request) {
        WarehouseNo warehouseNo = WarehouseNo.of(request.getWarehouseNo());
        Operator operator = Operator.of(request.getOperatorNo());
        TaskNo taskNo = TaskNo.of(request.getTaskNo());

        return ApiResponse.ofOk("");
    }

    /**
     * 操作员领取复核任务.
     */
    @KeyUsecase(in = "containerNo")
    public ApiResponse<Void> claimTask(ClaimTaskRequest request) throws WmsException {
        WarehouseNo warehouseNo = WarehouseNo.of(request.getWarehouseNo());
        Operator operator = Operator.of(request.getOperatorNo());
        ContainerNo containerNo = ContainerNo.of(request.getContainerNo());
        PlatformNo platformNo = PlatformNo.of(request.getPlatformNo());

        Task task = taskRepository.mustGet(containerNo, warehouseNo);
        task.assureSatisfied(new TaskCanPerformChecking()
                .and(new OperatorCannotBePicker(masterDataGateway, operator)));

        task.bind(operator, platformNo);
        uow.persist(task);

        return ApiResponse.ofOk();
    }

    /**
     * 操作员(复核员)清点并确认货品数量.
     *
     * <p>作业维度：(taskNo, orderNo, skuNo)</p>
     * <p>即：某个任务的下某个订单的某种货品，它确实可以发货{n}件/each，因为他们的(质量，数量)都OK.</p>
     */
    @KeyUsecase(in = {"taskNo", "orderNo", "skuNo", "qty"})
    public ApiResponse<Void> confirmQty(ConfirmQtyRequest request) throws WmsException {
        WarehouseNo warehouseNo = WarehouseNo.of(request.getWarehouseNo());
        Operator operator = Operator.of(request.getOperatorNo());
        OrderNo orderNo = OrderNo.of(request.getOrderNo());
        Sku sku = Sku.of(request.getSkuNo());
        BigDecimal qty = new BigDecimal(request.getQty());

        // 推荐复核台强约束？

        Task task = taskRepository.mustGet(TaskNo.of(request.getTaskNo()), orderNo, sku, warehouseNo);
        task.confirmQty(qty, operator, PlatformNo.of(request.getPlatformNo()));

        uow.persist(task);

        return ApiResponse.ofOk();
    }
}
