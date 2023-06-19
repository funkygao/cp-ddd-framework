package io.github.dddplus.runtime.registry;

import io.github.dddplus.testing.AloneRunner;
import io.github.dddplus.testing.AloneWith;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

// JUnit在整个project中是一个java进程，InternalIndexer 涉及静态变量，会导致两个单元测试类之间相互影响
// 最有效直接的办法就是每个测试用例都使用一个独立的classloader，即每个test case都是类隔离的
// https://stackoverflow.com/questions/42102/using-different-classloaders-for-different-junit-tests
@RunWith(AloneRunner.class)
@AloneWith(JUnit4.class)
public class ErrorOnPurposeTest {

    private ClassPathXmlApplicationContext applicationContext;

    @After
    public void tearDown() {
        if (applicationContext != null) {
            applicationContext.close();
            applicationContext = null;
        }

        InternalIndexer.domainDefMap.clear();
        InternalIndexer.domainStepDefMap.clear();
        InternalIndexer.routerDefMap.clear();
        InternalIndexer.partnerDefMap.clear();
        InternalIndexer.patternDefMap.clear();
        InternalIndexer.policyDefMap.clear();
        InternalIndexer.extensionInterceptor = null;
    }

    @Test
    public void dupDomain() {
        try {
            applicationContext = new ClassPathXmlApplicationContext("dup-domain.xml");
            fail();
        } catch (BeanCreationException expected) {
            assertEquals("duplicated domain code: FooDomain", expected.getCause().getMessage());
        }
    }

    @Test
    public void dupPattern() {
        try {
            applicationContext = new ClassPathXmlApplicationContext("dup-pattern.xml");
            fail();
        } catch (BeanCreationException expected) {
            assertEquals("duplicated pattern code: b2b", expected.getCause().getMessage());
        }
    }

    // 一个扩展点对应的IPolicy只能有一个，如果定义多了：DupTriggerPolicy/TriggerPolicy，拒绝启动
    @Test
    public void dupPolicy() {
        try {
            applicationContext = new ClassPathXmlApplicationContext("dup-policy.xml");
            fail();
        } catch (BeanCreationException expected) {
            assertEquals("1 Policy decides only 1 Extension:io.github.errcase.policy.DupTriggerPolicy, ext:io.github.dddplus.runtime.registry.mock.ext.ITrigger", expected.getCause().getMessage());
        }
    }

    // Pattern必须实现IIdentityResolver
    @Test
    public void invalidPattern() {
        try {
            applicationContext = new ClassPathXmlApplicationContext("pattern-invalid.xml");
            fail();
        } catch (BeanCreationException expected) {
            assertEquals("io.github.errcase.invalidpattern.InvalidPattern MUST implements IIdentityResolver", expected.getCause().getMessage());
        }
    }

    @Test
    public void dupPartner() {
        try {
            applicationContext = new ClassPathXmlApplicationContext("dup-partner.xml");
            fail();
        } catch (BeanCreationException expected) {
            assertEquals("duplicated partner code: ddd.cn.ka", expected.getCause().getMessage());
        }
    }

    @Test
    public void dupStep() {
        try {
            applicationContext = new ClassPathXmlApplicationContext("dup-step.xml");
            fail();
        } catch (BeanCreationException expected) {
            assertEquals("duplicated step code: Foo", expected.getCause().getMessage());
        }
    }

    @Test
    @Ignore
    public void abilityWithInvalidDomain() {
        try {
            applicationContext = new ClassPathXmlApplicationContext("ability-with-invalid-domain.xml");
            fail();
        } catch (BeanCreationException expected) {
            assertEquals("Router domain not found: non-exist", expected.getCause().getMessage());
        }
    }

    @Test
    public void serviceWithInvalidDomain() {
        try {
            applicationContext = new ClassPathXmlApplicationContext("service-with-invalid-domain.xml");
            fail();
        } catch (BeanCreationException expected) {
            assertEquals("DomainService domain not found: non-exist", expected.getCause().getMessage());
        }
    }

}
