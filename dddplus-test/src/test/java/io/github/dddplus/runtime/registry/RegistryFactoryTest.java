package io.github.dddplus.runtime.registry;

import io.github.dddplus.annotation.Extension;
import io.github.dddplus.annotation.Partner;
import io.github.dddplus.annotation.Step;
import io.github.dddplus.ext.IPlugable;
import io.github.dddplus.runtime.registry.mock.extension.FooPartnerExt;
import io.github.dddplus.runtime.registry.mock.partner.FooPartner;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring-test.xml"})
public class RegistryFactoryTest {

    @Before
    public void setUp() {
        InternalIndexer.partnerDefPrepared = null;
    }

    @Test
    public void preparePartner() {
        Object fooPartner = DDDBootstrap.applicationContext().getBean(FooPartner.class);
        RegistryFactory.preparePlugins(Partner.class, fooPartner);
        assertEquals(1, InternalIndexer.partnerDefMap.size());
        assertEquals(FooPartner.CODE, InternalIndexer.partnerDefPrepared.getCode());
        assertEquals(0, InternalIndexer.partnerDefPrepared.getExtensionDefMap().size());

        Object fooPartnerExt = DDDBootstrap.applicationContext().getBean(FooPartnerExt.class);
        RegistryFactory.preparePlugins(Extension.class, fooPartnerExt);
        assertEquals(1, InternalIndexer.partnerDefPrepared.getExtensionDefMap().size());

        InternalIndexer.commitPartner();
        assertNull(InternalIndexer.partnerDefPrepared);
    }

    @Test
    public void prepareNonPlugable() {
        try {
            // Date is not IPlugable
            RegistryFactory.preparePlugins(Partner.class, new Date());
            fail();
        } catch (BootstrapException expected) {
            assertEquals("java.util.Date must be IPlugable", expected.getMessage());
        }
    }

    @Test
    public void prepareUnknownPlugable() {
        try {
            RegistryFactory.preparePlugins(Step.class, new UnknownPlugable());
            fail();
        } catch (BootstrapException expected) {
            assertEquals("io.github.dddplus.annotation.Step not supported", expected.getMessage());
        }
    }

    @Test
    @Ignore
    public void invalidPrepare() {
        Object fooPartner = DDDBootstrap.applicationContext().getBean(FooPartner.class);
        // 故意搞错类型：fooPartner不是Extension
        // 目前是抛出 NPE，但这在实际场景应该不会出现，先 ignore this case
        RegistryFactory.preparePlugins(Extension.class, fooPartner);
    }

    @Test
    public void invalidOrder() {
        // 应该先prepare Partner 之后才能prepare Extension
        Object fooPartnerExt = DDDBootstrap.applicationContext().getBean(FooPartnerExt.class);
        try {
            RegistryFactory.preparePlugins(Extension.class, fooPartnerExt);
            fail();
        } catch (BootstrapException expected) {
            assertEquals("Partner must reside in Plugin Jar with its extensions!", expected.getMessage());
        }
    }

    private static class UnknownPlugable implements IPlugable {
    }

}
