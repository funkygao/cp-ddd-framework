package ddd.plus.showcase.wms.application.service;

import ddd.plus.showcase.wms.application.UnitOfWork;
import ddd.plus.showcase.wms.application.convert.CartonAppConverter;
import ddd.plus.showcase.wms.application.convert.TaskAppConverter;
import ddd.plus.showcase.wms.application.service.dto.*;
import ddd.plus.showcase.wms.application.service.dto.base.ApiResponse;
import ddd.plus.showcase.wms.application.worker.dto.SubmitTaskDto;
import ddd.plus.showcase.wms.domain.carton.*;
import ddd.plus.showcase.wms.domain.carton.ext.ConsumableExtPolicy;
import ddd.plus.showcase.wms.domain.carton.spec.CartonNotFull;
import ddd.plus.showcase.wms.domain.common.*;
import ddd.plus.showcase.wms.domain.common.gateway.IMasterDataGateway;
import ddd.plus.showcase.wms.domain.common.gateway.IOrderGateway;
import ddd.plus.showcase.wms.domain.common.publisher.IEventPublisher;
import ddd.plus.showcase.wms.domain.order.*;
import ddd.plus.showcase.wms.domain.order.dict.OrderType;
import ddd.plus.showcase.wms.domain.order.spec.OrderNotCartonizedYet;
import ddd.plus.showcase.wms.domain.order.spec.OrderNotFullyCartonized;
import ddd.plus.showcase.wms.domain.order.spec.OrderUseOnePack;
import ddd.plus.showcase.wms.domain.order.spec.OrderUsesManualCheckFlow;
import ddd.plus.showcase.wms.domain.task.*;
import ddd.plus.showcase.wms.domain.task.dict.TaskMode;
import ddd.plus.showcase.wms.domain.task.spec.OperatorCannotBePicker;
import ddd.plus.showcase.wms.domain.task.spec.TaskCanPerformChecking;
import ddd.plus.showcase.wms.domain.task.spec.UniqueCodeConstraint;
import ddd.plus.showcase.wms.infrastructure.domain.task.ContainerItemPo;
import ddd.plus.showcase.wms.infrastructure.domain.task.TaskManager;
import io.github.dddplus.dsl.KeyUsecase;
import io.github.dddplus.model.IApplicationService;
import io.github.dddplus.runtime.DDD;
import io.github.design.ContainerNo;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.*;

/**
 * 业务用例：人工复核.
 * <p>
 * <p>仅做{@code DDDplus}正向建模演示使用，因此无关细节被省略.</p>
 * <p>通过它，可以掌握{@code DDDplus}绝大部分正向建模方法，以及逆向建模{@code DSL}标注方法.</p>
 */
@Service
@Setter(onMethod_ = {@Resource})
@Slf4j
public class CheckingAppService implements IApplicationService {
    private IMasterDataGateway masterDataGateway;
    private IOrderGateway orderGateway;
    private Comparator<Platform> comparator;
    private Random random = new Random();
    private ITaskRepository taskRepository;
    private IUuidRepository uuidRepository;
    private IOrderRepository orderRepository;
    private ICartonRepository cartonRepository;
    private TaskManager taskManager;
    private ISequencer sequencer;
    private IEventPublisher eventPublisher;
    private UnitOfWork uow;

    // 提交复核任务
    @KeyUsecase
    public ApiResponse<String> submitTask(@Valid SubmitTaskDto dto) {
        WarehouseNo warehouseNo = WarehouseNo.of(dto.getWarehouseNo());

        // 幂等性
        Uuid uuid = Uuid.of(dto.getUuid(), Uuid.Type.Task, warehouseNo, uuidRepository);
        if (uuid.exists()) {
            return ApiResponse.ofOk(uuid.getBizNo());
        }

        Task task = TaskAppConverter.INSTANCE.fromDto(dto); // dangling object
        OrderBag pendingOrderBag = task.orders().pendingOrders();
        // 任务下发过程中，客户可能取消订单了
        OrderBagCanceled canceledOrderBag = pendingOrderBag.canceledBag(orderGateway);
        // 从任务中移除这部分订单行
        task.removeOrderLines(canceledOrderBag.orderLineNos());
        if (task.isEmpty()) {
            return ApiResponse.ofOk(null);
        }

        task.allocateTaskNo(TaskNo.of(sequencer.next()));
        uuid.setBizNo(task.getTaskNo().value());
        task.enrichSkuInfo(masterDataGateway);
        task.plan();
        task.accept(eventPublisher);

        uow.persist(task, canceledOrderBag, uuid);
        return ApiResponse.ofOk(task.getTaskNo().value());
    }

    public ApiResponse<String> recommendPlatform(@Valid RecommendPlatformRequest request) {
        if (request.getOrderNo() != null) {
            Platform platform = recommendPlatformByOrder(request);
            return ApiResponse.ofOk(platform.value());
        } else {
            Platform platform = recommendPlatformByTaskBacklog(request);
            return ApiResponse.ofOk(platform.value());
        }
    }

    /**
     * 提升拣货员去哪个复核台：按任务积压情况
     */
    @KeyUsecase(in = {"taskNo"})
    private Platform recommendPlatformByTaskBacklog(RecommendPlatformRequest request) {
        List<Platform> platforms = masterDataGateway.candidatePlatforms(
                OrderType.valueOf(request.getOrderType()),
                TaskMode.valueOf(request.getTaskMode()),
                WarehouseNo.of(request.getWarehouseNo()));
        if (platforms.size() == 1) {
            // bingo!
            return platforms.get(0);
        }

        // sort and find the best according to backlog
        Map<Platform, List<Task>> taskMap = taskRepository.pendingTasksOfPlatforms(platforms);
        platforms.forEach(platform -> {
            TaskBag taskBag = TaskBag.of(taskMap.get(platform));
            platform.setFinishedWorkload(taskBag.totalCheckedQty());
            platform.setBacklog(taskBag.totalPendingQty());
        });

        platforms = new ArrayList<>(taskMap.keySet());
        platforms.stream().sorted(comparator);
        return platforms.get(0);
    }

    /**
     * 提升拣货员去哪个复核台：按订单
     */
    @KeyUsecase(in = {"orderNo"})
    private Platform recommendPlatformByOrder(RecommendPlatformRequest request) {
        WarehouseNo warehouseNo = WarehouseNo.of(request.getWarehouseNo());
        Order order = orderRepository.mustGet(OrderNo.of(request.getOrderNo()), warehouseNo);
        Platform platform = order.recommendedPlatform();
        if (platform.isPresent()) {
            // 这个订单之前已经推荐了，仍使用原来的复核台
            return platform;
        }

        // 通过order.tasks()这个关联对象指针获取该订单的所有任务很直观
        // 如果 TaskRepository.listTasks(OrderNo)，这种关联关系被断开
        TaskBag tasksOfOrder = order.tasks().taskBag();
        List<Platform> platforms = tasksOfOrder.platforms();
        if (!platforms.isEmpty()) {
            // shuffle
            return platforms.get(random.nextInt(platforms.size()));
        }

        // 业务兜底
        return recommendPlatformByTaskBacklog(request);
    }

    /**
     * 为任务的某个纸箱推荐耗材种类和数量
     */
    @KeyUsecase(in = "cartonNo")
    public ApiResponse<Void> recommendConsumable(@Valid RecommendConsumableRequest request) {
        WarehouseNo warehouseNo = WarehouseNo.of(request.getWarehouseNo());
        Carton carton = cartonRepository.mustGet(CartonNo.of(request.getCartonNo()), warehouseNo);
        Task task = taskRepository.mustGet(TaskNo.of(request.getTaskNo()), warehouseNo);
        ConsumableBag consumableBag = DDD.usePolicy(ConsumableExtPolicy.class, task)
                .recommendFor(task, carton);
        if (consumableBag == null) {
            // 没有命中场景：不推荐耗材
        }

        return ApiResponse.ofOk();
    }

    /**
     * 复核员扫描容器领取复核任务.
     */
    @KeyUsecase(in = "containerNo")
    public ApiResponse<Integer> claimTask(@Valid ClaimTaskRequest request) throws WmsException {
        Operator operator = Operator.of(request.getOperatorNo());

        TaskOfContainerPending taskOfContainerPending = taskRepository.mustGet(
                ContainerNo.of(request.getContainerNo()),
                WarehouseNo.of(request.getWarehouseNo()));
        taskOfContainerPending.assureSatisfied(new TaskCanPerformChecking()
                .and(new OperatorCannotBePicker(masterDataGateway, operator)));
        taskOfContainerPending.claimedWith(operator, Platform.of(request.getPlatformNo()));

        // 通过关联对象加载引用关系的聚合
        OrderBag pendingOrderBag = taskOfContainerPending.orders().pendingOrders();
        pendingOrderBag.satisfy(new OrderUsesManualCheckFlow());
        // 逆向物流逻辑，在领任务时控制运营成本最低
        OrderBagCanceled canceledOrderBag = pendingOrderBag.canceledBag(orderGateway);

        uow.persist(taskOfContainerPending, canceledOrderBag);

        if (request.getRecommendPackQty()) {
            return ApiResponse.ofOk(pendingOrderBag.anyOne().recommendPackQty());
        }

        return ApiResponse.ofOk(0);
    }

    /**
     * 复核装箱一体化：按货品维度.
     */
    @KeyUsecase(in = {"skuNo", "qty"})
    public ApiResponse<Void> checkBySku(@Valid CheckBySkuRequest request) throws WmsException {
        WarehouseNo warehouseNo = WarehouseNo.of(request.getWarehouseNo());
        Operator operator = Operator.of(request.getOperatorNo());
        OrderNo orderNo = OrderNo.of(request.getOrderNo());
        BigDecimal qty = new BigDecimal(request.getQty());

        Task task = taskRepository.mustGet(TaskNo.of(request.getTaskNo()),
                orderNo, Sku.of(request.getSkuNo()), warehouseNo);
        task.assureSatisfied(new TaskCanPerformChecking()
                .and(new UniqueCodeConstraint(UniqueCode.of(request.getUniqueCode())))
                .and(new OperatorCannotBePicker(masterDataGateway, operator)));

        Order order = task.orders().pendingOrder(orderNo);
        order.assureSatisfied(new OrderNotFullyCartonized());

        // 此时复核员已经领取任务了，客单是不允许取消的，因此不必检查逆向逻辑

        CheckResult checkResult = task.confirmQty(qty, operator,
                Platform.of(request.getPlatformNo()));

        // 装箱，物理世界里，复核员已经清点数量，并把货品从容器里转移到箱，但人可能放错货品，运营要管控
        Carton carton = cartonRepository.mustGet(CartonNo.of(request.getCartonNo()), warehouseNo);
        carton.assureSatisfied(new CartonNotFull()
                .and(carton.cartonizationRule()));
        carton.bindOrder(orderNo, qty);
        carton.transferFrom(checkResult);

        uow.persist(task, carton);
        return ApiResponse.ofOk();
    }

    /**
     * 把一个出库单的所有货品一次性放到入参指定的纸箱：爆品订单复核
     */
    @KeyUsecase(in = {"orderNo"})
    public ApiResponse<Void> checkByOrder(@Valid CheckByOrderRequest request) throws WmsException {
        WarehouseNo warehouseNo = WarehouseNo.of(request.getWarehouseNo());
        Operator operator = Operator.of(request.getOperatorNo());

        Order order = orderRepository.mustGet(OrderNo.of(request.getOrderNo()), warehouseNo);
        order.assureSatisfied(new OrderUseOnePack()
                .and(new OrderNotCartonizedYet()));

        TaskOfOrderPending taskOfOrderPending = taskRepository.mustGet(order.getOrderNo(), warehouseNo);
        taskOfOrderPending.confirmQty(operator, Platform.of(request.getPlatformNo()));

        CartonBag cartonBag = CartonBag.of(CartonAppConverter.INSTANCE.fromDto(request));
        cartonBag.fulfill(Operator.of(request.getOperatorNo()), Platform.of(request.getPlatformNo()));

        if (request.getPalletNo() != null) {
            // 物理世界里：把这些纸箱放到的某一个栈板上
            PalletNo palletNo = PalletNo.of(request.getPalletNo());
            cartonBag.putOnPallet(palletNo);
        }

        order.checkedBy(operator);

        if (order.constraint().isCollectConsumables()) {
            cartonBag.deductConsumableInventory();
        }

        // ...
        uow.persist(taskOfOrderPending, order, cartonBag);
        return ApiResponse.ofOk();
    }

    /**
     * 复核员把拣货容器的货品放入箱，并使用耗材以便运输安全，该过程发现箱已满.
     */
    @KeyUsecase(in = {"orderNo", "cartonNo", "consumables"})
    public ApiResponse<Void> fulfillCarton(@Valid CartonFullRequest request) throws WmsException {
        Carton carton = cartonRepository.mustGet(CartonNo.of(request.getCartonNo()), WarehouseNo.of(request.getWarehouseNo()));
        carton.assureSatisfied(new CartonNotFull());
        if (carton.isEmpty()) {
            // 复核员说这个空箱满了？
        }

        Order order = carton.order().get();
        if (order.constraint().isCollectConsumables()) {
            // 该订单要记录使用了哪些耗材，以便独立核算成本
            carton.installConsumables(null); // request里定义耗材信息，mapstruct转换：这样了省略细节
        }

        carton.fulfill(Operator.of(request.getOperatorNo()), Platform.of(request.getPlatformNo()));

        uow.persist(carton);
        return ApiResponse.ofOk();
    }

    // 查询、报表类用例，跳过domain层
    public ApiResponse<List<ContainerItemResult>> listContainerItems(String taskNo, String warehouseNo) {
        List<ContainerItemPo> containerItemPoList = taskManager.listContainerItemsBy(TaskNo.of(taskNo), WarehouseNo.of(warehouseNo));
        return ApiResponse.ofOk(TaskAppConverter.INSTANCE.containerItemPoList2Dto(containerItemPoList));
    }
}
