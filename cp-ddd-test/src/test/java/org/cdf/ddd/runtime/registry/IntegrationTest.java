package org.cdf.ddd.runtime.registry;

import org.cdf.ddd.runtime.registry.mock.ability.*;
import org.cdf.ddd.runtime.registry.mock.exception.FooException;
import org.cdf.ddd.runtime.registry.mock.partner.FooPartner;
import org.cdf.ddd.runtime.registry.mock.service.FooDomainService;
import org.cdf.ddd.model.ExtTimeoutException;
import org.cdf.ddd.runtime.DDD;
import org.cdf.ddd.runtime.registry.mock.MockStartupListener;
import org.cdf.ddd.runtime.registry.mock.step.*;
import org.cdf.ddd.runtime.registry.mock.ext.IMultiMatchExt;
import org.cdf.ddd.runtime.registry.mock.model.FooModel;
import org.cdf.ddd.runtime.registry.mock.pattern.extension.B2BMultiMatchExt;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
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
    private SubmitStepsExec submitStepsExec;

    private FooModel fooModel;

    @Resource
    private MockStartupListener startupListener;

    @Before
    public void setUp() {
        fooModel = new FooModel();
        fooModel.setProjectCode(FooPartner.CODE);
        fooModel.setB2c(true);

        // ExtensionInvocationHandler.extInvokeTimerExecutor的线程池缩小到10，方便并发测试
        System.setProperty("invokeExtMaxPoolSize", "10");
    }

    @Test
    public void startupListener() {
        assertTrue(startupListener.isCalled());
    }

    @Test
    public void findDomainAbility() {
        FooDomainAbility fooDomainAbility = InternalIndexer.findDomainAbility(FooDomainAbility.class);
        assertNotNull(fooDomainAbility);

        DomainAbilityDef domainAbilityDef = new DomainAbilityDef();
        try {
            domainAbilityDef.registerBean(fooDomainAbility);
            fail();
        } catch (BootstrapException expected) {
            assertTrue(expected.getMessage().startsWith("duplicated domain ability:"));
        }

        // 它没有加DomainAbility注解，是无法找到的
        IllegalDomainAbility illegalDomainAbility = InternalIndexer.findDomainAbility(IllegalDomainAbility.class);
        assertNull(illegalDomainAbility);
    }

    @Test
    public void noImplementationExt() {
        NotImplementedAbility ability = DDD.findAbility(NotImplementedAbility.class);
        assertNotNull(ability);
        ability.ping(fooModel);
    }

    @Test
    public void noImplementationExtAndNoDefaultExt() {
        NotImplementedAbility1 ability = DDD.findAbility(NotImplementedAbility1.class);
        assertNotNull(ability);
        ability.ping(fooModel);
    }

    @Test
    public void reducerFirstOf() {
        BarDomainAbility ability = DDD.findAbility(BarDomainAbility.class);
        String result = ability.submit(fooModel);
        // submit里执行了Reducer.firstOf，对应的扩展点是：BarExt, ProjectExt
        // 应该返回ProjectExt的结果
        assertEquals("2", result);
    }

    @Test
    public void reducerAll() {
        BarDomainAbility ability = DDD.findAbility(BarDomainAbility.class);
        String result = ability.submit2(fooModel);
        log.info("{}", result);
        // 由于submit2里执行了Reducer.all，返回值被置为null了
        assertNull(result);
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
        model.setProjectCode("unit test");
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
    public void abilityThrowsException() {
        try {
            BarDomainAbility ability = DDD.findAbility(BarDomainAbility.class);
            ability.throwsEx(fooModel);
        } catch (RuntimeException expected) {

        }
    }

    @Test
    public void integrationTest() {
        // domain service -> domain ability -> extension
        // ProjectExt
        fooDomainService.submitOrder(fooModel);
    }

    @Test(expected = RuntimeException.class)
    public void extThrowException() {
        // B2BExt
        fooModel.setProjectCode("");
        fooModel.setB2c(false);
        fooDomainService.submitOrder(fooModel);
    }

    @Test
    public void callExtTimeout() {
        // B2BExt
        fooModel.setProjectCode("");
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
        fooModel.setProjectCode("");
        fooModel.setB2c(false);
        fooModel.setWillSleepLong(true);
        fooModel.setWillThrowRuntimeException(true);
        fooDomainService.submitOrder(fooModel);
    }

    @Test
    public void callExtThrownUnexpectedException() {
        // B2BExt
        fooModel.setProjectCode("");
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
        fooModel.setProjectCode("");
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
        assertEquals(198, DDD.findAbility(BazAbility.class).guess(fooModel).intValue());
    }

    @Test
    public void patterPriority() {
        // IMultiMatchExt在B2BPattern、FooPattern上都有实现，而B2BPattern的priority最小，因此应该返回它的实例
        fooModel.setProjectCode("foo"); // 匹配 FooPattern
        fooModel.setB2c(false); // 匹配 B2BPattern
        List<ExtensionDef> extensions = InternalIndexer.findEffectiveExtensions(IMultiMatchExt.class, fooModel, true);
        assertEquals(1, extensions.size());
        Assert.assertEquals(B2BMultiMatchExt.class, extensions.get(0).getExtensionBean().getClass());
    }

    @Test
    public void decideSteps() {
        // fooModel不是B2B模式，匹配不了B2BDecideStepsExt
        assertNull(DDD.findAbility(DecideStepsAbility.class).decideSteps(fooModel, Steps.Submit.Activity));

        fooModel.setB2c(false);
        // B2BDecideStepsExt
        List<String> b2bSubmitSteps = DDD.findAbility(DecideStepsAbility.class).decideSteps(fooModel, Steps.Submit.Activity);
        assertEquals(2, b2bSubmitSteps.size());
    }

    @Test
    public void stepsExecTemplate() {
        fooModel.setB2c(false);
        fooModel.setRedecide(true);
        fooModel.setStepsRevised(false);
        List<String> steps = DDD.findAbility(DecideStepsAbility.class).decideSteps(fooModel, Steps.Submit.Activity);
        // B2BDecideStepsExt: FooStep -> BarStep(if redecide then add Baz & Ham) -> BazStep -> HamStep
        submitStepsExec.execute(Steps.Submit.Activity, steps, fooModel);
        assertTrue(fooModel.isStepsRevised());
    }

    @Test
    public void stepsRevisionDeadLoop() {
        fooModel.setB2c(false);
        fooModel.setRedecideDeadLoop(true); // dead loop on purpose
        fooModel.setStepsRevised(false);
        List<String> steps = DDD.findAbility(DecideStepsAbility.class).decideSteps(fooModel, Steps.Submit.Activity);
        // B2BDecideStepsExt: FooStep -> BarStep(if redecideDeadLoop then add BarStep)
        try {
            submitStepsExec.execute(Steps.Submit.Activity, steps, fooModel);
            fail();
        } catch (RuntimeException expected) {
            assertEquals("Seems steps dead loop, abort after 100", expected.getMessage());
        }
    }

    @Test
    public void stepsExecTemplateWithRollback() {
        fooModel.setB2c(false);
        fooModel.setWillRollback(true);
        List<String> steps = DDD.findAbility(DecideStepsAbility.class).decideSteps(fooModel, Steps.Submit.Activity);
        log.info("steps: {}", steps);
        try {
            submitStepsExec.execute(Steps.Submit.Activity, steps, fooModel);
            fail();
        } catch (FooException expected) {
            assertEquals(BarStep.rollbackReason, expected.getMessage());
        }
    }

    // Step抛出它IDomainRevokableStep定义的异常类型，才会触发rollback。抛出其他异常，不会回滚
    @Test
    public void stepsExecTemplateWithInvalidRollback() {
        fooModel.setB2c(false);
        fooModel.setWillRollbackInvalid(true);
        List<String> steps = DDD.findAbility(DecideStepsAbility.class).decideSteps(fooModel, Steps.Submit.Activity);
        log.info("steps: {}", steps);
        try {
            submitStepsExec.execute(Steps.Submit.Activity, steps, fooModel);
            fail();
        } catch (RuntimeException expected) {
            assertFalse(expected instanceof FooException);
            assertEquals("Will not rollback", expected.getMessage());
        }
    }

}
