package io.github.spring;

import org.junit.jupiter.api.Disabled;

import static org.junit.jupiter.api.Assertions.assertTrue;

//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = Application.class)
public class SpringAutowireTest {

    //@Test
    @Disabled
    public void byUtil() {
        InboundOrder order = new InboundOrder();
        ApplicationContextUtil.autowire(order);
        assertTrue(order.beans().startsWith("["));
    }
}
