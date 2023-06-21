package io.github.dddplus;

import io.github.dddplus.runtime.DDD;
import io.github.design.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

// mock DDD类的静态方法
public class MockDDDPlusTest {
    MockedStatic<DDD> ddd;
    @Mock
    SplitOrderExtRouter router;
    @Mock
    ISplitOrderExt splitOrderExt;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ddd = Mockito.mockStatic(DDD.class);
    }

    @AfterEach
    public void close() {
        ddd.close();
    }

    // 演示如何mock DDD.useRouter
    @Test
    void demoHowToMockDDDWhenUseRouter() {
        CheckTask task = CheckTask.builder().build();
        ShipmentOrder order = ShipmentOrder.builder().build();

        // mock router
        doThrow(new RuntimeException("mocked")).when(router).splitOrderMutuallyExclusive(any(), any());
        // mock DDD
        ddd.when(() -> DDD.useRouter(any())).thenReturn(router);

        // 对使用了DDD.useXxx的方法进行集成测试
        try {
            DDD.useRouter(SplitOrderExtRouter.class).splitOrderMutuallyExclusive(order, task);
            fail();
        } catch (RuntimeException expected) {
            assertEquals("mocked", expected.getMessage());
        }
    }

    @Test
    void demoHowToMockDDDWhenUsePolicy() {
        CheckTask task = CheckTask.builder().build();
        ShipmentOrder order = ShipmentOrder.builder().build();

        // mock扩展点实现
        doThrow(new RuntimeException("mocked by policy")).when(splitOrderExt).split(any(), any());
        ddd.when(() -> DDD.usePolicy(any(), any())).thenReturn(splitOrderExt);

        try {
            DDD.usePolicy(SplitOrderExtPolicy.class, order).split(order, task);
            fail();
        } catch (RuntimeException expected) {
            assertEquals("mocked by policy", expected.getMessage());
        }
    }

    @Test
    void callServiceWithDDDMock() {
        CheckTask task = CheckTask.builder().build();
        ShipmentOrder order = ShipmentOrder.builder().build();

        // mock扩展点实现
        doThrow(new RuntimeException("mocked by x")).when(splitOrderExt).split(any(), any());
        ddd.when(() -> DDD.usePolicy(any(), any())).thenReturn(splitOrderExt);

        MyService myService = new MyService();
        try {
            myService.doSth(order, task);
            fail();
        } catch (RuntimeException expected) {
            assertEquals("mocked by x", expected.getMessage());
        }

    }
}
