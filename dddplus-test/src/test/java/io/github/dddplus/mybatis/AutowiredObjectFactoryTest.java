package io.github.dddplus.mybatis;

import io.github.design.CheckTask;
import io.github.design.mybatis.FactoryWrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring-test-design.xml"})
public class AutowiredObjectFactoryTest {

    @Resource
    private FactoryWrapper autowireObjectFactory;

    @Test
    public void basic() {
        CheckTask checkTask = autowireObjectFactory.create(CheckTask.class);
        checkTask.helo();
        assertEquals(checkTask.helo(), "bye");
    }

}