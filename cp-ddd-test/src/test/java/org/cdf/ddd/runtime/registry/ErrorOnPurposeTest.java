package org.cdf.ddd.runtime.registry;

import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

// 每个测试方法单独跑可以，但一起跑ErrorOnPurposeTest就失败：
// 1. InternalIndexer的索引数据都是static的
// 2. DDDBootstrap.once是静态的
@Ignore
public class ErrorOnPurposeTest {

    private ClassPathXmlApplicationContext applicationContext;

    @After
    public void tearDown() {
        if (applicationContext != null) {
            applicationContext.destroy();
            applicationContext = null;
        }
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

    // Pattern必须实现IIdentityResolver
    @Test
    public void invalidPattern() {
        try {
            applicationContext = new ClassPathXmlApplicationContext("pattern-invalid.xml");
            fail();
        } catch (BeanCreationException expected) {
            assertEquals("org.cdf.errcase.invalidpattern.InvalidPattern MUST implements IIdentityResolver", expected.getCause().getMessage());
        }
    }

    @Test
    public void dupPartner() {
        try {
            applicationContext = new ClassPathXmlApplicationContext("dup-partner.xml");
            fail();
        } catch (BeanCreationException expected) {
            assertEquals("duplicated partner code: jdl.cn.ka", expected.getCause().getMessage());
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
    public void abilityWithInvalidDomain() {
        try {
            applicationContext = new ClassPathXmlApplicationContext("ability-with-invalid-domain.xml");
            fail();
        } catch (BeanCreationException expected) {
            assertEquals("DomainAbility domain not found: non-exist", expected.getCause().getMessage());
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
