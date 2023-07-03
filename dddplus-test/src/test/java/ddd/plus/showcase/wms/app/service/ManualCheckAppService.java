package ddd.plus.showcase.wms.app.service;

import ddd.plus.showcase.wms.app.UnitOfWork;
import ddd.plus.showcase.wms.app.service.dto.*;
import ddd.plus.showcase.wms.app.service.dto.base.ApiResponse;
import ddd.plus.showcase.wms.domain.carton.Carton;
import ddd.plus.showcase.wms.domain.carton.CartonNo;
import ddd.plus.showcase.wms.domain.carton.ICartonRepository;
import ddd.plus.showcase.wms.domain.carton.spec.CaronNotFull;
import ddd.plus.showcase.wms.domain.common.*;
import ddd.plus.showcase.wms.domain.order.*;
import ddd.plus.showcase.wms.domain.order.spec.OrderNotFullyCartonized;
import ddd.plus.showcase.wms.domain.order.spec.OrderUsesManualCheckFlow;
import ddd.plus.showcase.wms.domain.task.ITaskRepository;
import ddd.plus.showcase.wms.domain.task.PlatformNo;
import ddd.plus.showcase.wms.domain.task.Task;
import ddd.plus.showcase.wms.domain.task.TaskNo;
import ddd.plus.showcase.wms.domain.task.spec.OperatorCannotBePicker;
import ddd.plus.showcase.wms.domain.task.spec.TaskCanPerformChecking;
import ddd.plus.showcase.wms.domain.task.spec.TaskCanRecheck;
import ddd.plus.showcase.wms.domain.task.spec.UniqueCodeConstraint;
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
    private OrderGateway orderGateway;

    private ITaskRepository taskRepository;
    private IOrderRepository orderRepository;
    private ICartonRepository cartonRepository;

    private UnitOfWork uow;

    /**
     * 操作员应该去哪一个复核台?
     */
    @KeyUsecase(in = "taskNo")
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

        // 该容器还没复核完，把它的任务加载
        Task task = taskRepository.mustGetPending(containerNo, warehouseNo);
        task.assureSatisfied(new TaskCanPerformChecking()
                .and(new OperatorCannotBePicker(masterDataGateway, operator)));
        task.claimedWith(operator, platformNo);

        // 通过association对象加载管理聚合根
        OrderBag pendingOrders = task.pendingOrders();
        pendingOrders.assureSatisfied(new OrderUsesManualCheckFlow());
        // 逆向物流逻辑
        OrderBagCanceled canceledOrderBag = pendingOrders.subBagOfCanceled(orderGateway);

        uow.persist(task, canceledOrderBag);
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

        Task task = taskRepository.mustGetPending(TaskNo.of(request.getTaskNo()), orderNo, sku, warehouseNo);
        task.assureSatisfied(new TaskCanPerformChecking()
                .and(new UniqueCodeConstraint(UniqueCode.of(request.getUniqueCode())))
                .and(new OperatorCannotBePicker(masterDataGateway, operator)));

        Order order = task.pendingOrder(orderNo);
        order.assureSatisfied(new OrderNotFullyCartonized());

        task.confirmQty(qty, operator, PlatformNo.of(request.getPlatformNo()));

        // 装箱
        Carton carton = cartonRepository.mustGet(CartonNo.of(request.getCartonNo()), warehouseNo);
        carton.assureSatisfied(new CaronNotFull());

        uow.persist(task);

        return ApiResponse.ofOk();
    }

    /**
     * 暂停一个出库单.
     */
    @KeyUsecase(in = "orderNo")
    public ApiResponse<Void> pause(PauseOrderRequest request) throws WmsException {
        Order order = orderRepository.mustGet(OrderNo.of(request.getOrderNo()), WarehouseNo.of(request.getWarehouseNo()));
        order.pause(Operator.of(request.getOperatorNo()));
        orderRepository.save(order);
        return ApiResponse.ofOk();
    }

    /**
     * 恢复一个出库单的执行.
     */
    @KeyUsecase(in = "orderNo")
    public ApiResponse<Void> resume(ResumeOrderRequest request) throws WmsException {
        Order order = orderRepository.mustGet(OrderNo.of(request.getOrderNo()), WarehouseNo.of(request.getWarehouseNo()));
        order.resume(Operator.of(request.getOperatorNo()));
        orderRepository.save(order);
        return ApiResponse.ofOk();
    }

    /**
     * 重新复核.
     */
    @KeyUsecase(in = {"taskNo", "cartonNo"})
    public ApiResponse<Void> recheck(RecheckRequest request) throws WmsException {
        TaskNo taskNo = TaskNo.of(request.getTaskNo());
        CartonNo cartonNo = CartonNo.of(request.getCartonNo());
        Operator operator = Operator.of(request.getOperatorNo());

        Task task = taskRepository.mustGetPending(taskNo, WarehouseNo.of(request.getWarehouseNo()));
        task.assureSatisfied(new TaskCanRecheck());

        return ApiResponse.ofOk();
    }

    /**
     * 复核员把拣货容器的货品放入箱，并使用耗材以便运输安全，该过程发现箱已满.
     */
    @KeyUsecase(in = {"orderNo", "cartonNo", "consumables"})
    public ApiResponse<Void> cartonFull(CartonFullRequest request) throws WmsException {
        return ApiResponse.ofOk();
    }
}
