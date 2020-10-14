package io.github.dddplus.runtime.registry;

import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@Ignore
@PrepareForTest // PowerMock create class loader per method，解决InternalIndexer内部static state无法测试的问题
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
            assertEquals("io.github.errcase.invalidpattern.InvalidPattern MUST implements IIdentityResolver", expected.getCause().getMessage());
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
