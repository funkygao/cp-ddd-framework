package org.cdf.ddd.runtime.registry;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

// TODO 每个测试方法单独跑可以，但一起跑ErrorOnPurposeTest就失败
@Ignore
public class ErrorOnPurposeTest {

    private ClassPathXmlApplicationContext applicationContext;

    @Test
    public void dupDomain() {
        try {
            applicationContext = new ClassPathXmlApplicationContext("dup-domain.xml");
            fail();
        } catch (BeanCreationException expected) {
            assertEquals("duplicated domain code: FooDomain", expected.getCause().getMessage());
        } finally {
            if (applicationContext != null) {
                applicationContext.stop();
            }
        }
    }

    @Test
    public void dupPattern() {
        try {
            applicationContext = new ClassPathXmlApplicationContext("dup-pattern.xml");
            fail();
        } catch (BeanCreationException expected) {
            assertEquals("duplicated pattern code: b2b", expected.getCause().getMessage());
        } finally {
            if (applicationContext != null) {
                applicationContext.stop();
            }
        }
    }

    // Pattern必须实现IIdentityResolver
    @Test
    public void invalidPattern() {
        try {
            applicationContext = new ClassPathXmlApplicationContext("dup-pattern-invalid.xml");
            fail();
        } catch (BeanCreationException expected) {
            assertEquals("org.cdf.errcase.invalidpattern.InvalidPattern MUST implements IIdentityResolver", expected.getCause().getMessage());
        } finally {
            if (applicationContext != null) {
                applicationContext.stop();
            }
        }
    }

    @Test
    public void dupPartner() {
        try {
            applicationContext = new ClassPathXmlApplicationContext("dup-partner.xml");
            fail();
        } catch (BeanCreationException expected) {
            assertEquals("duplicated partner code: jdl.cn.ka", expected.getCause().getMessage());
        } finally {
            if (applicationContext != null) {
                applicationContext.stop();
            }
        }
    }

    @Test
    public void dupStep() {
        try {
            applicationContext = new ClassPathXmlApplicationContext("dup-step.xml");
            fail();
        } catch (BeanCreationException expected) {
            assertEquals("duplicated step code: Foo", expected.getCause().getMessage());
        } finally {
            if (applicationContext != null) {
                applicationContext.stop();
            }
        }
    }

    @Test
    public void abilityWithInvalidDomain() {
        try {
            applicationContext = new ClassPathXmlApplicationContext("ability-with-invalid-domain.xml");
            fail();
        } catch (BeanCreationException expected) {
            assertEquals("DomainAbility domain not found: non-exist", expected.getCause().getMessage());
        } finally {
            if (applicationContext != null) {
                applicationContext.stop();
            }
        }
    }

    @Test
    public void serviceWithInvalidDomain() {
        try {
            applicationContext = new ClassPathXmlApplicationContext("service-with-invalid-domain.xml");
            fail();
        } catch (BeanCreationException expected) {
            assertEquals("DomainService domain not found: non-exist", expected.getCause().getMessage());
        } finally {
            if (applicationContext != null) {
                applicationContext.stop();
            }
        }
    }

}
