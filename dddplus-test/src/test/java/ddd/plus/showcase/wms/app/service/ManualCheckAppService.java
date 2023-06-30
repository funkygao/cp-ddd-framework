package ddd.plus.showcase.wms.app.service;

import ddd.plus.showcase.wms.app.UnitOfWork;
import ddd.plus.showcase.wms.app.service.dto.ClaimTaskRequest;
import ddd.plus.showcase.wms.app.service.dto.ConfirmQtyRequest;
import ddd.plus.showcase.wms.app.service.dto.RecommendPlatformRequest;
import ddd.plus.showcase.wms.app.service.dto.base.ApiResponse;
import ddd.plus.showcase.wms.domain.common.MasterDataGateway;
import ddd.plus.showcase.wms.domain.common.Operator;
import ddd.plus.showcase.wms.domain.common.WarehouseNo;
import ddd.plus.showcase.wms.domain.common.WmsException;
import ddd.plus.showcase.wms.domain.order.OrderNo;
import ddd.plus.showcase.wms.domain.task.PlatformNo;
import ddd.plus.showcase.wms.domain.task.Task;
import ddd.plus.showcase.wms.domain.task.TaskNo;
import ddd.plus.showcase.wms.domain.task.TaskRepository;
import ddd.plus.showcase.wms.domain.task.spec.OperatorCannotBePicker;
import ddd.plus.showcase.wms.domain.task.spec.TaskCanPerformChecking;
import io.github.design.ContainerNo;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Setter(onMethod_ = {@Resource})
@Slf4j
public class ManualCheckAppService {
    private MasterDataGateway masterDataGateway;
    private TaskRepository taskRepository;
    private UnitOfWork uow;

    /**
     * 操作员在执行一个复核任务，他应该去哪一个复核台?
     */
    public ApiResponse<String> recommenPlatform(RecommendPlatformRequest request) {
        WarehouseNo warehouseNo = WarehouseNo.of(request.getWarehouseNo());
        Operator operator = Operator.of(request.getOperatorNo());
        TaskNo taskNo = TaskNo.of(request.getTaskNo());

        return ApiResponse.ofOk("");
    }

    /**
     * 操作员领取复核任务.
     */
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
     */
    public ApiResponse<Void> confirmQty(ConfirmQtyRequest request) throws WmsException {
        WarehouseNo warehouseNo = WarehouseNo.of(request.getWarehouseNo());
        Operator operator = Operator.of(request.getOperatorNo());
        OrderNo orderNo = OrderNo.of(request.getOrderNo());

        Task task = taskRepository.mustGet(TaskNo.of(request.getTaskNo()), warehouseNo);

        return ApiResponse.ofOk();
    }
}
