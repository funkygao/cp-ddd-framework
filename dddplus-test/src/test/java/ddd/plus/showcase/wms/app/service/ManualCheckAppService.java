package ddd.plus.showcase.wms.app.service;

import ddd.plus.showcase.wms.app.UnitOfWork;
import ddd.plus.showcase.wms.app.convert.CartonAppConverter;
import ddd.plus.showcase.wms.app.service.dto.*;
import ddd.plus.showcase.wms.app.service.dto.base.ApiResponse;
import ddd.plus.showcase.wms.domain.carton.Carton;
import ddd.plus.showcase.wms.domain.carton.CartonNo;
import ddd.plus.showcase.wms.domain.carton.ICartonRepository;
import ddd.plus.showcase.wms.domain.carton.spec.CaronNotFull;
import ddd.plus.showcase.wms.domain.common.*;
import ddd.plus.showcase.wms.domain.common.gateway.IMasterDataGateway;
import ddd.plus.showcase.wms.domain.common.gateway.IOrderGateway;
import ddd.plus.showcase.wms.domain.common.flow.RecommendPlatformFlow;
import ddd.plus.showcase.wms.domain.order.*;
import ddd.plus.showcase.wms.domain.order.spec.OrderNotCartonizedYet;
import ddd.plus.showcase.wms.domain.order.spec.OrderNotFullyCartonized;
import ddd.plus.showcase.wms.domain.order.spec.OrderUseOnePack;
import ddd.plus.showcase.wms.domain.order.spec.OrderUsesManualCheckFlow;
import ddd.plus.showcase.wms.domain.task.*;
import ddd.plus.showcase.wms.domain.task.spec.OperatorCannotBePicker;
import ddd.plus.showcase.wms.domain.task.spec.TaskCanPerformChecking;
import ddd.plus.showcase.wms.domain.task.spec.TaskCanRecheck;
import ddd.plus.showcase.wms.domain.task.spec.UniqueCodeConstraint;
import io.github.dddplus.dsl.KeyUsecase;
import io.github.dddplus.model.IApplicationService;
import io.github.design.ContainerNo;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * 业务用例：人工复核.
 * <p>
 * <p>仅做{@code DDDplus}正向建模演示使用，因此无关细节被省略.</p>
 * <p>通过它，可以掌握{@code DDDplus}绝大部分正向建模方法，以及逆向建模{@code DSL}标注方法.</p>
 */
@Service
@Setter(onMethod_ = {@Resource})
@Slf4j
public class ManualCheckAppService implements IApplicationService {
    private IMasterDataGateway masterDataGateway;
    private IOrderGateway orderGateway;

    private ITaskRepository taskRepository;
    private IOrderRepository orderRepository;
    private ICartonRepository cartonRepository;
    private UnitOfWork uow;

    private RecommendPlatformFlow recommendPlatformFlow;

    /**
     * 拣货员应该去哪一个复核台进行复核?
     */
    @KeyUsecase(in = "taskNo")
    public ApiResponse<String> recommendPlatform(RecommendPlatformRequest request) {
        Platform platformNo = recommendPlatformFlow.execute(request);
        return ApiResponse.ofOk(platformNo.value());
    }

    /**
     * 复核员扫描容器领取复核任务.
     */
    @KeyUsecase(in = "containerNo")
    public ApiResponse<Integer> claimTask(ClaimTaskRequest request) throws WmsException {
        WarehouseNo warehouseNo = WarehouseNo.of(request.getWarehouseNo());
        Operator operator = Operator.of(request.getOperatorNo());
        ContainerNo containerNo = ContainerNo.of(request.getContainerNo());
        Platform platformNo = Platform.of(request.getPlatformNo());

        // 该容器还没复核完，把它的任务加载
        TaskOfContainer containerTask = taskRepository.mustGetPending(containerNo, warehouseNo);
        containerTask.unbounded().assureSatisfied(new TaskCanPerformChecking()
                .and(new OperatorCannotBePicker(masterDataGateway, operator)));
        containerTask.unbounded().claimedWith(operator, platformNo);

        // 通过association对象加载管理聚合根
        OrderBag pendingOrders = containerTask.unbounded().pendingOrders();
        pendingOrders.satisfy(new OrderUsesManualCheckFlow());
        // 逆向物流逻辑
        OrderBagCanceled canceledOrderBag = pendingOrders.subBagOfCanceled(orderGateway);

        uow.persist(containerTask, canceledOrderBag);

        if (request.getRecommendPackQty()) {
            return ApiResponse.ofOk(pendingOrders.anyItem().recommendPackQty());
        }

        return ApiResponse.ofOk(0);
    }

    /**
     * 复核装箱一体化：按货品维度.
     * <p>
     * <p>作业维度：(taskNo, orderNo, skuNo)</p>
     * <p>即：某个任务的下某个订单的某种货品，它确实可以发货{n}件/each，因为他们的(质量，数量)都OK.</p>
     */
    @KeyUsecase(in = {"skuNo", "qty"})
    public ApiResponse<Void> checkBySku(CheckBySkuRequest request) throws WmsException {
        WarehouseNo warehouseNo = WarehouseNo.of(request.getWarehouseNo());
        Operator operator = Operator.of(request.getOperatorNo());
        OrderNo orderNo = OrderNo.of(request.getOrderNo());
        Sku sku = Sku.of(request.getSkuNo());
        BigDecimal qty = new BigDecimal(request.getQty());

        // 推荐复核台强约束？

        TaskOfSku taskOfSku = taskRepository.mustGetPending(TaskNo.of(request.getTaskNo()), orderNo, sku, warehouseNo);
        taskOfSku.unbounded().assureSatisfied(new TaskCanPerformChecking()
                .and(new UniqueCodeConstraint(UniqueCode.of(request.getUniqueCode())))
                .and(new OperatorCannotBePicker(masterDataGateway, operator)));

        Order order = taskOfSku.unbounded().pendingOrder(orderNo);
        order.assureSatisfied(new OrderNotFullyCartonized());

        ContainerItemBag checkResult = taskOfSku.confirmQty(qty, operator, Platform.of(request.getPlatformNo()));

        // 装箱，物理世界里，复核员已经清点数量，并把货品从容器里转移到箱，但人可能放错货品，运营要管控
        Carton carton = cartonRepository.mustGet(CartonNo.of(request.getCartonNo()), warehouseNo);
        carton.assureSatisfied(new CaronNotFull()
                .and(carton.cartonizationRule())); // 业务规则本身也可以是规约
        carton.bindOrder(orderNo, qty);
        carton.transferFrom(checkResult);

        uow.persist(taskOfSku, carton);
        return ApiResponse.ofOk();
    }

    /**
     * 把一个出库单的所有货品一次性放到入参指定的纸箱：爆品订单复核
     */
    @KeyUsecase(in = {"orderNo"})
    public ApiResponse<Void> checkByOrder(CheckByOrderRequest request) throws WmsException {
        WarehouseNo warehouseNo = WarehouseNo.of(request.getWarehouseNo());
        Order order = orderRepository.mustGet(OrderNo.of(request.getOrderNo()), warehouseNo);
        order.assureSatisfied(new OrderUseOnePack()
                .and(new OrderNotCartonizedYet()));

        TaskOfOrder taskOfOrder = taskRepository.mustGet(order.getOrderNo(), warehouseNo);
        taskOfOrder.confirmQty(Operator.of(request.getOperatorNo()), Platform.of(request.getPlatformNo()));

        List<Carton> cartonList = CartonAppConverter.INSTANCE.fromDto(request);
        cartonList.stream().forEach(c -> c.fulfill(Operator.of(request.getOperatorNo()), Platform.of(request.getPlatformNo())));
        // ...
        return ApiResponse.ofOk();
    }

    /**
     * 重新复核.
     */
    @KeyUsecase(in = {"taskNo", "cartonNo"})
    public ApiResponse<Void> recheck(RecheckRequest request) throws WmsException {
        Task task = taskRepository.mustGetPending(TaskNo.of(request.getTaskNo()), WarehouseNo.of(request.getWarehouseNo()));
        task.assureSatisfied(new TaskCanRecheck());
        return ApiResponse.ofOk();
    }

    /**
     * 复核员把拣货容器的货品放入箱，并使用耗材以便运输安全，该过程发现箱已满.
     */
    @KeyUsecase(in = {"orderNo", "cartonNo", "consumables"})
    public ApiResponse<Void> fulfillCarton(CartonFullRequest request) throws WmsException {
        Carton carton = cartonRepository.mustGet(CartonNo.of(request.getCartonNo()), WarehouseNo.of(request.getWarehouseNo()));
        carton.assureSatisfied(new CaronNotFull());
        if (carton.isEmpty()) {
            // 复核员说这个空箱满了？
        }

        Order order = carton.order().get();
        if (order.constraint().isCollectConsumables()) {
            // 该订单要记录使用了哪些耗材，以便独立核算成本
            carton.useConsumables(null); // request里定义耗材信息，mapstruct转换：这样了省略细节
        }

        carton.fulfill(Operator.of(request.getOperatorNo()), Platform.of(request.getPlatformNo()));

        uow.persist(carton);
        return ApiResponse.ofOk();
    }
}
