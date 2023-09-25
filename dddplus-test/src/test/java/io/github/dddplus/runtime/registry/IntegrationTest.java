package io.github.dddplus.runtime.registry;

import io.github.dddplus.ext.IDecideStepsExt;
import io.github.dddplus.runtime.DDD;
import io.github.dddplus.runtime.ExtTimeoutException;
import io.github.dddplus.runtime.StepsExecTemplate;
import io.github.dddplus.runtime.policy.ConsumableExtPolicy;
import io.github.dddplus.runtime.policy.IConsumableExt;
import io.github.dddplus.runtime.policy.MyBusinessException;
import io.github.dddplus.runtime.policy.SKU;
import io.github.dddplus.runtime.registry.mock.MockStartupListener;
import io.github.dddplus.runtime.registry.mock.domain.FooDomain;
import io.github.dddplus.runtime.registry.mock.exception.FooException;
import io.github.dddplus.runtime.registry.mock.ext.*;
import io.github.dddplus.runtime.registry.mock.extension.B2CExt;
import io.github.dddplus.runtime.registry.mock.model.FooModel;
import io.github.dddplus.runtime.registry.mock.model.SaleOrder;
import io.github.dddplus.runtime.registry.mock.partner.FooPartner;
import io.github.dddplus.runtime.registry.mock.pattern.extension.B2BMultiMatchExt;
import io.github.dddplus.runtime.registry.mock.policy.TriggerPolicy;
import io.github.dddplus.runtime.registry.mock.router.*;
import io.github.dddplus.runtime.registry.mock.service.FooDomainService;
import io.github.dddplus.runtime.registry.mock.step.BarStep;
import io.github.dddplus.runtime.registry.mock.step.EggStep;
import io.github.dddplus.runtime.registry.mock.step.Steps;
import io.github.dddplus.runtime.registry.mock.step.SubmitStep;
import io.github.dddplus.testing.LogAssert;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.SchedulingTaskExecutor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.RejectedExecutionException;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring-test.xml"})
@Slf4j
public class IntegrationTest {

    @Resource
    private FooDomainService fooDomainService;

    @Autowired
    protected ApplicationContext ctx;

    @Resource
    private SchedulingTaskExecutor asyncStepsExecutor;

    @Resource
    private SchedulingTaskExecutor asyncStepsExecutorAutoDiscard;

    @Autowired
    private StepsExecTemplate<SubmitStep, FooModel> submitStepsExec; // https://jira.springsource.org/browse/SPR-9965

    private FooModel fooModel;

    @Resource
    private MockStartupListener startupListener;

    @Before
    public void setUp() {
        fooModel = new FooModel();
        fooModel.setPartnerCode(FooPartner.CODE);
        fooModel.setB2c(true);

        // ExtensionInvocationHandler.extInvokeTimerExecutor的线程池缩小到10，方便并发测试
        System.setProperty("invokeExtMaxPoolSize", "10");
        // GovernanceAspect
        System.setProperty("appName", "foo");
    }

    @Test
    public void startupListener() {
        assertTrue(startupListener.isCalled());
    }

    @Test
    public void findRouter() {
        FooRouter fooRouter = InternalIndexer.findRouter(FooRouter.class);
        assertNotNull(fooRouter);

        RouterDef routerDef = new RouterDef();
        try {
            routerDef.registerBean(fooRouter);
            fail();
        } catch (BootstrapException expected) {
            assertTrue(expected.getMessage().startsWith("duplicated router:"));
        }

        // 它没有加Router注解，是无法找到的
        IllegalRouter illegalRouter = InternalIndexer.findRouter(IllegalRouter.class);
        assertNull(illegalRouter);

        assertNotNull(DDD.useRouter(PartnerRouter.class));
    }

    @Test
    public void noImplementationExt() {
        NotImplementedRouter router = DDD.useRouter(NotImplementedRouter.class);
        assertNotNull(router);
        router.ping(fooModel);
    }

    @Test
    public void noImplementationExtAndNoDefaultExt() {
        NotImplementedRouter1 router = DDD.useRouter(NotImplementedRouter1.class);
        assertNotNull(router);
        router.ping(fooModel);
    }

    @Test
    public void reducerFirstOf() {
        BarRouter router = DDD.useRouter(BarRouter.class);
        String result = router.submit(fooModel);
        // submit里执行了Reducer.firstOf，对应的扩展点是：B2CExt, PartnerExt
        // 应该返回 PartnerExt 的结果
        assertEquals("2", result);
    }

    @Test
    public void blindlyRunAllExt() {
        log.info("blindlyRunAllExt 1");
        DDD.useRouter(PullbackExtRouter.class).blindlyExecuteAllExt(new SaleOrder());
        log.info("blindlyRunAllExt 2");
    }

    @Test
    public void reducerAll() {
        BarRouter router = DDD.useRouter(BarRouter.class);
        String result = router.submit2(fooModel);
        assertEquals(String.valueOf(B2CExt.RESULT), result);
    }

    @Test
    public void findDomainSteps() {
        List<String> codes = new ArrayList<>();
        codes.add(Steps.Submit.FooStep);
        codes.add(Steps.Submit.BarStep);
        codes.add(Steps.Cancel.EggStep); // 它属于CancelStep，而不属于SubmitStep
        List<StepDef> stepDefs = InternalIndexer.findDomainSteps(Steps.Submit.Activity, codes);
        assertEquals(2, stepDefs.size());
        assertEquals("foo活动", stepDefs.get(0).getName());
    }

    @Test
    public void wronglyFetchActivityCase() {
        // FooStep -> SubmitStep
        // EggStep -> CancelStep
        // 故意写错
        try {
            SubmitStep theStep = DDD.getStep(Steps.Cancel.Activity, Steps.Cancel.EggStep);
            fail();
        } catch (java.lang.ClassCastException expected) {
            // EggStep$$EnhancerBySpringCGLIB$$21d4da4f cannot be cast to SubmitStep
        }

        EggStep eggStep = DDD.getStep(Steps.Cancel.Activity, Steps.Cancel.EggStep);
        eggStep.execute(fooModel);
    }

    @Test
    public void dddFindSteps() {
        List<String> codes = new ArrayList<>();
        codes.add(Steps.Submit.FooStep);
        codes.add(Steps.Submit.BarStep);
        codes.add(Steps.Cancel.EggStep);
        List<SubmitStep> activities = DDD.findSteps(Steps.Submit.Activity, codes);
        assertEquals(2, activities.size());

        SubmitStep barStep = DDD.getStep(Steps.Submit.Activity, Steps.Submit.BarStep);
        assertNotNull(barStep);
        assertTrue(barStep instanceof BarStep);
        assertSame(activities.get(1), barStep);

        FooModel model = new FooModel();
        model.setPartnerCode("unit test");
        log.info("will execute steps...");
        for (SubmitStep step : activities) {
            step.execute(model);
        }
    }

    @Test
    public void dddFindInvalidStep() {
        assertNull(DDD.getStep(Steps.Submit.Activity, "invalid-step-code"));
    }

    @Test
    public void routerThrowsException() {
        try {
            BarRouter router = DDD.useRouter(BarRouter.class);
            router.throwsEx(fooModel);
            fail();
        } catch (RuntimeException expected) {
            assertEquals(BarRouter.EX, expected.getMessage());
        }
    }

    @Test
    public void usePolicy() {
        DDD.usePolicy(TriggerPolicy.class, fooModel).beforeInsert(fooModel);
        fooModel.setFoo(1);
        DDD.usePolicy(TriggerPolicy.class, fooModel).afterInsert(fooModel);
    }

    @Test
    public void directGetExtension() {
        // 不通过 BaseRouter，直接获取扩展点实例
        Integer result = DDD.firstExtension(IFooExt.class, fooModel).execute(fooModel);
        assertEquals(B2CExt.RESULT, result.intValue());
    }

    @Test
    public void testPolicy() throws IOException {
        fooModel.setFoo(1);
        DDD.firstExtension(ITrigger.class, fooModel).beforeInsert(fooModel);
        LogAssert.assertContains("foo trigger");

        fooModel.setFoo(2);
        DDD.firstExtension(ITrigger.class, fooModel).beforeInsert(fooModel);
        LogAssert.assertContains("bar trigger");

        DDD.usePolicy(TriggerPolicy.class, fooModel).afterInsert(fooModel);
    }

    @Test
    public void businessException() {
        String skuValue = "2";
        SKU sku = new SKU(skuValue);
        try {
            DDD.usePolicy(ConsumableExtPolicy.class, sku).recommend(skuValue);
            fail();
        } catch (MyBusinessException expected) {
            assertEquals("hi 2", expected.getMessage());
        }
    }

    @Test
    public void policy_inheritance() {
        String skuValue = "1";
        SKU sku = new SKU(skuValue);
        assertEquals(DDD.usePolicy(ConsumableExtPolicy.class, sku).recommend(skuValue), "CON-10");
    }

    @Test
    public void ext_inheritance() {
        String skuValue = "1";
        SKU sku = new SKU(skuValue);
        // 小件
        assertEquals(DDD.firstExtension(IConsumableExt.class, sku).recommend(skuValue), "CON-10");
        skuValue = "10";
        sku = new SKU(skuValue);
        assertEquals(DDD.firstExtension(IConsumableExt.class, sku).recommend(skuValue), "CON-11");

        // 大件
        skuValue = "20";
        sku = new SKU(skuValue);
        assertEquals("CON-211", DDD.firstExtension(IConsumableExt.class, sku).recommend(skuValue));

        // illegal sku
        skuValue = "invalid";
        sku = new SKU(skuValue);
        assertNull(DDD.firstExtension(IConsumableExt.class, sku).recommend(skuValue));
    }

    @Test
    public void policyHasNoExtensionInstance() {
        fooModel.setFoo(3);
        DDD.firstExtension(ITrigger.class, fooModel).beforeInsert(fooModel);

        fooModel.setFoo(4);
        DDD.firstExtension(ITrigger.class, fooModel).beforeInsert(fooModel);
    }

    @Test
    public void integrationTest() {
        // domain service -> domain router -> extension
        // PartnerExt
        fooDomainService.submitOrder(fooModel);
    }

    @Test(expected = RuntimeException.class)
    public void extThrowException() {
        // B2BExt
        fooModel.setPartnerCode("");
        fooModel.setB2c(false);
        fooDomainService.submitOrder(fooModel);
    }

    @Test
    public void callExtTimeout() {
        // B2BExt
        fooModel.setPartnerCode("");
        fooModel.setB2c(false);
        fooModel.setWillSleepLong(true);
        try {
            fooDomainService.submitOrder(fooModel);
            fail();
        } catch (ExtTimeoutException expected) {
            assertEquals("timeout:500ms", expected.getMessage());
        }
    }

    @Test(expected = RuntimeException.class)
    public void callExtWithTimeoutAndThrownException() {
        // B2BExt
        fooModel.setPartnerCode("");
        fooModel.setB2c(false);
        fooModel.setWillSleepLong(true);
        fooModel.setWillThrowRuntimeException(true);
        fooDomainService.submitOrder(fooModel);
    }

    @Test
    public void callExtThrownUnexpectedException() {
        // B2BExt
        fooModel.setPartnerCode("");
        fooModel.setB2c(false);
        fooModel.setWillThrowOOM(true);
        try {
            fooDomainService.submitOrder(fooModel);
            fail();
        } catch (OutOfMemoryError expected) {
            assertEquals("OOM on purpose", expected.getMessage());
        }
    }

    @Test
    public void extensionInvokeTimeoutThreadPoolExhausted() throws InterruptedException {
        final int threadCount = 12;
        CountDownLatch latch = new CountDownLatch(threadCount);

        fooModel = new FooModel();
        fooModel.setPartnerCode("");
        fooModel.setB2c(false); // B2BExt
        fooModel.setWillSleepLong(true);

        log.info("WILL exhaust the timer pool...");

        List<SubmitOrderTask> tasks = new ArrayList<>(threadCount);

        // will trigger RejectedExecutionException
        for (int i = 0; i < threadCount; i++) {
            tasks.add(new SubmitOrderTask(i, latch, fooModel, fooDomainService));
        }

        for (SubmitOrderTask task : tasks) {
            Thread thread = new Thread(task);
            thread.setName("SubmitOrderTask-" + task.idx);
            thread.start();
        }

        latch.await();

        int rejectN = 0;
        for (SubmitOrderTask task : tasks) {
            if (task.rejected) {
                rejectN++;
            }
        }

        assertEquals(threadCount - 10, rejectN);
    }

    private static class SubmitOrderTask implements Runnable {
        public int idx;
        private CountDownLatch latch;
        private FooModel fooModel;
        private FooDomainService fooDomainService;
        public boolean rejected;

        SubmitOrderTask(int idx, CountDownLatch latch, FooModel fooModel, FooDomainService fooDomainService) {
            this.idx = idx;
            this.latch = latch;
            this.fooModel = fooModel;
            this.fooDomainService = fooDomainService;
        }

        @Override
        public void run() {
            try {

                fooDomainService.submitOrder(fooModel);
            } catch (RejectedExecutionException expected) {
                log.info("as expected, thread pool full: {}", expected.getMessage());
                rejected = true;
            } catch (ExtTimeoutException expected) {
                assertEquals("timeout:500ms", expected.getMessage());
            }

            latch.countDown();
        }
    }

    @Test
    public void defaultExtensionComponent() {
        assertEquals(198, DDD.useRouter(BazRouter.class).guess(fooModel).intValue());
    }

    @Test
    public void patternPriority() {
        // IMultiMatchExt在B2BPattern、FooPattern上都有实现，而B2BPattern的priority最小，因此应该返回它的实例
        fooModel.setPartnerCode("foo"); // 匹配 FooPattern
        fooModel.setB2c(false); // 匹配 B2BPattern
        List<ExtensionDef> extensions = InternalIndexer.findEffectiveExtensions(IMultiMatchExt.class, fooModel, true);
        assertEquals(1, extensions.size());
        Assert.assertEquals(B2BMultiMatchExt.class, extensions.get(0).getExtensionBean().getClass());
    }

    @Test
    public void decideSteps() {
        // fooModel不是B2B模式，匹配不了B2BDecideStepsExt
        assertNotNull(DDD.useRouter(DecideStepsRouter.class).decideSteps(fooModel, Steps.Submit.Activity));
        assertEquals(0, DDD.useRouter(DecideStepsRouter.class).decideSteps(fooModel, Steps.Submit.Activity).size());

        fooModel.setB2c(false);
        // B2BDecideStepsExt
        List<String> b2bSubmitSteps = DDD.useRouter(DecideStepsRouter.class).decideSteps(fooModel, Steps.Submit.Activity);
        assertEquals(3, b2bSubmitSteps.size());

        try {
            DDD.useRouter(DecideStepsRouter.class).decideSteps(null, Steps.Submit.Activity);
            fail();
        } catch (NullPointerException expected) {
        }
        try {
            DDD.useRouter(DecideStepsRouter.class).defaultExtension(null);
            fail();
        } catch (NullPointerException expected) {

        }
    }

    @Test
    public void stepsExecTemplate() throws IOException {
        fooModel.setB2c(false);
        fooModel.setRedecide(true);
        fooModel.setStepsRevised(false);
        List<String> steps = DDD.useRouter(DecideStepsRouter.class).decideSteps(fooModel, Steps.Submit.Activity);
        // B2BDecideStepsExt: FooStep -> BarStep(if redecide then add Baz & Ham) -> BazStep -> HamStep
        submitStepsExec.execute(Steps.Submit.Activity, steps, fooModel);
        assertTrue(fooModel.isStepsRevised());

        // empty steps case, will do nothing
        fooModel.setStepsRevised(false);
        submitStepsExec.execute(Steps.Submit.Activity, null, fooModel);
        assertFalse(fooModel.isStepsRevised());
        submitStepsExec.execute(Steps.Submit.Activity, new ArrayList<>(), fooModel);
        assertFalse(fooModel.isStepsRevised());

        // AOP式step interceptors test
        LogAssert.assertContains("AROUND step:Submit.Baz", "AROUND step:Submit.Foo", "AROUND step:Submit.Bar");
    }

    @Test
    public void stepsExecTemplateOneStepTimeoutWillNotRollback() {
        fooModel.setB2c(false);
        fooModel.setRedecide(false);
        fooModel.setStepsRevised(false);
        fooModel.setSleepExtTimeout(true);
        List<String> steps = DDD.useRouter(DecideStepsRouter.class).decideSteps(fooModel, Steps.Submit.Activity);
        // BazStep FooStep
        // FooStep调用一个扩展点超时，会抛出 ExtTimeoutException，不确定状态：抛出到外面，框架层不做回滚
        try {
            submitStepsExec.execute(Steps.Submit.Activity, steps, fooModel);
            fail();
        } catch (ExtTimeoutException expected) {
            assertEquals("timeout:100ms", expected.getMessage());
        }
    }

    @Test
    public void stepsExecTemplateForAsync() {
        fooModel.setB2c(false);
        fooModel.setRedecide(true);
        fooModel.setStepsRevised(false);
        List<String> steps = DDD.useRouter(DecideStepsRouter.class).decideSteps(fooModel, Steps.Submit.Activity);
        // B2BDecideStepsExt: FooStep -> BarStep(if redecide then add Baz & Ham) -> BazStep -> HamStep
        Set<String> asyncSteps = new HashSet<>();
        submitStepsExec.execute(Steps.Submit.Activity, steps, fooModel, null, asyncSteps);
        assertTrue(fooModel.isStepsRevised());
        submitStepsExec.execute(Steps.Submit.Activity, steps, fooModel, asyncStepsExecutor, null);
        assertTrue(fooModel.isStepsRevised());

        asyncSteps.add(Steps.Submit.BazStep);
        submitStepsExec.execute(Steps.Submit.Activity, steps, fooModel, asyncStepsExecutor, asyncSteps);
        assertTrue(fooModel.isStepsRevised());
    }

    @Test
    public void asyncStepThreadPoolFullCase() {
        fooModel.setB2c(false);
        fooModel.setWillSleepLong(true);
        fooModel.setRedecide(false);
        fooModel.setStepsRevised(false);
        List<String> steps = DDD.useRouter(DecideStepsRouter.class).decideSteps(fooModel, Steps.Submit.Activity);
        // B2BDecideStepsExt: FooStep -> BarStep(if redecide then add Baz & Ham) -> BazStep -> HamStep
        Set<String> asyncSteps = new HashSet<>();
        asyncSteps.add(Steps.Submit.FooStep);
        asyncSteps.add(Steps.Submit.BarStep);
        asyncSteps.add(Steps.Submit.BazStep);
        try {
            submitStepsExec.execute(Steps.Submit.Activity, steps, fooModel, asyncStepsExecutor, asyncSteps);
            fail();
        } catch (RejectedExecutionException expected) {
        }
    }

    @Test
    public void asyncStepThreadPoolFullDiscardPolicyCase() {
        fooModel.setB2c(false);
        fooModel.setWillSleepLong(true);
        fooModel.setRedecide(false);
        fooModel.setStepsRevised(false);
        List<String> steps = DDD.useRouter(DecideStepsRouter.class).decideSteps(fooModel, Steps.Submit.Activity);
        log.info("steps:{}", steps); // Baz, Foo, Bar
        Set<String> asyncSteps = new HashSet<>();
        asyncSteps.add(Steps.Submit.FooStep);
        asyncSteps.add(Steps.Submit.BarStep);
        asyncSteps.add(Steps.Submit.BazStep);
        submitStepsExec.execute(Steps.Submit.Activity, steps, fooModel, asyncStepsExecutorAutoDiscard, asyncSteps);
    }

    @Test
    public void asyncStepThrownExceptionWillBeIgnored() throws InterruptedException {
        fooModel.setB2c(false);
        fooModel.setWillSleepLong(false);
        fooModel.setRedecide(false);
        fooModel.setStepsRevised(false);
        fooModel.setLetFooThrowException(true);
        List<String> steps = DDD.useRouter(DecideStepsRouter.class).decideSteps(fooModel, Steps.Submit.Activity);
        log.info("steps:{}", steps); // Baz, Foo, Bar
        Set<String> asyncSteps = new HashSet<>();
        asyncSteps.add(Steps.Submit.FooStep);
        asyncSteps.add(Steps.Submit.BazStep);
        // FooStep会抛出异常，但会被忽略
        submitStepsExec.execute(Steps.Submit.Activity, steps, fooModel, asyncStepsExecutorAutoDiscard, asyncSteps);
        Thread.sleep(1000);
    }

    @Test
    public void stepsRevisionDeadLoop() {
        fooModel.setB2c(false);
        fooModel.setRedecideDeadLoop(true); // dead loop on purpose
        fooModel.setStepsRevised(false);
        List<String> steps = DDD.useRouter(DecideStepsRouter.class).decideSteps(fooModel, Steps.Submit.Activity);
        // B2BDecideStepsExt: FooStep -> BarStep(if redecideDeadLoop then add BarStep)
        try {
            submitStepsExec.execute(Steps.Submit.Activity, steps, fooModel);
            fail();
        } catch (RuntimeException expected) {
            assertEquals("Seems steps dead loop, abort after 100", expected.getMessage());
        }
    }

    @Test
    public void stepsExecTemplateWithRollback() throws IOException {
        fooModel.setB2c(false);
        fooModel.setWillRollback(true);
        List<String> steps = DDD.useRouter(DecideStepsRouter.class).decideSteps(fooModel, Steps.Submit.Activity);
        log.info("steps: {}", steps); // Baz, Foo, Bar
        try {
            submitStepsExec.execute(Steps.Submit.Activity, steps, fooModel);
            fail();
        } catch (FooException expected) {
            assertEquals(BarStep.rollbackReason, expected.getMessage());
        }

        // BarStep执行时抛异常，确保 FooStep, BazStep 的rollback被调用
        LogAssert.assertContains("foo rollback, cause", "baz rollback for");
    }

    // Step抛出 IRevokableDomainStep 定义的异常类型，才会触发rollback。抛出其他异常，不会回滚
    @Test
    public void stepsExecTemplateWithInvalidRollback() {
        fooModel.setB2c(false);
        fooModel.setWillRollbackInvalid(true);
        List<String> steps = DDD.useRouter(DecideStepsRouter.class).decideSteps(fooModel, Steps.Submit.Activity);
        log.info("steps: {}", steps);
        try {
            submitStepsExec.execute(Steps.Submit.Activity, steps, fooModel);
            fail();
        } catch (RuntimeException expected) {
            assertFalse(expected instanceof FooException);
            assertEquals("Will not rollback", expected.getMessage());
        }
    }

    @Test
    public void exportDomainArtifacts() {
        DomainArtifacts artifacts = DomainArtifacts.getInstance();
        // domains
        assertEquals(1, artifacts.getDomains().size());
        assertEquals(FooDomain.CODE, artifacts.getDomains().get(0).getCode());

        // steps
        assertEquals(2, artifacts.getSteps().size());
        assertTrue(artifacts.getSteps().containsKey(Steps.Cancel.Activity));
        assertTrue(artifacts.getSteps().containsKey(Steps.Submit.Activity));
        List<DomainArtifacts.Step> submitSteps = artifacts.getSteps().get(Steps.Submit.Activity);
        assertEquals(4, submitSteps.size()); // FooStep, BarStep, BazStep, HamStep
        assertTrue(submitSteps.stream().anyMatch(step -> step.getTags().length > 0 &&
            step.getTags()[0].equals(Steps.Submit.GoodsValidationGroup)));

        // extensions: IFooExt IMultiMatchExt IReviseStepsExt IDecideStepsExt IPartnerExt IPatternOnlyExt
        assertEquals(7, artifacts.getExtensions().size());
        int foundExtN = 0;
        boolean foundPartnerOnlyPattern = false;
        boolean foundPatternOnlyPattern = false;
        for (DomainArtifacts.Extension extension : artifacts.getExtensions()) {
            if (IDecideStepsExt.class == extension.getExt()) {
                foundExtN++;

                // B2BPattern
                assertEquals(1, extension.getPatterns().size());
            }

            if (IMultiMatchExt.class == extension.getExt()) {
                foundExtN++;

                // B2BPattern, FooPattern
                assertEquals(2, extension.getPatterns().size());
            }

            if (IPatternOnlyExt.class == extension.getExt()) {
                foundPatternOnlyPattern = true;
                assertEquals(0, extension.getPartners().size());
                assertEquals(1, extension.getPatterns().size());
            }

            if (IPartnerExt.class == extension.getExt()) {
                foundPartnerOnlyPattern = true;

                assertEquals(0, extension.getPatterns().size());
                assertEquals(1, extension.getPartners().size()); // 只有 FooPartner 实现了该扩展点
            }

            if (IFooExt.class == extension.getExt()) {
                foundExtN++;

                // B2BPattern, FooPattern, B2CPattern
                assertEquals(3, extension.getPatterns().size());
            }
        }
        assertEquals(3, foundExtN);
        assertTrue(foundPatternOnlyPattern);
        assertTrue(foundPartnerOnlyPattern);
    }
}
