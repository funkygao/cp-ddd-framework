package io.github.dddplus;

import io.github.dddplus.runtime.pattern.PledgeAppService;
import org.junit.Test;

import static org.junit.Assert.*;

public class DDDPlusEnforcerTest {

    @Test
    public void identityResolverNotMarkedWithPatternOrPartner() {
        DDDPlusEnforcer enforcer = new DDDPlusEnforcer();
        enforcer.scanPackages("io.github.enforce.identity_resolver1");
        try {
            enforcer.enforce();
            fail();
        } catch (AssertionError expected) {
            assertTrue(expected.getMessage().contains("is not annotated with @Pattern"));
            assertTrue(expected.getMessage().contains("is not annotated with @Partner"));
        }
    }

    @Test
    public void patternNotResolverAllowed() {
        DDDPlusEnforcer enforcer = new DDDPlusEnforcer();
        enforcer.scanPackages(PledgeAppService.class.getPackage().getName());
        enforcer.enforce(); // will not throw validation exception
    }

    @Test
    public void badPatternAnnotationUsage() {
        DDDPlusEnforcer enforcer = new DDDPlusEnforcer();
        enforcer.scanPackages("io.github.enforce.basepattern");
        try {
            enforcer.enforce();
            fail();
        } catch (AssertionError expected) {
            assertTrue(expected.getMessage().contains("is not assignable to io.github.dddplus.ext.IIdentityResolver"));
        }
    }

    @Test
    public void extNotAnnotatedWithExtension() {
        DDDPlusEnforcer enforcer = new DDDPlusEnforcer();
        enforcer.scanPackages("io.github.enforce.ext_annotation");
        try {
            enforcer.enforce();
            fail();
        } catch (AssertionError expected) {
            assertTrue(expected.getMessage().contains("is not annotated with @Extension"));
        }
    }

    @Test
    public void extNaming() {
        DDDPlusEnforcer enforcer = new DDDPlusEnforcer();
        enforcer.scanPackages("io.github.enforce.ext_naming");
        try {
            enforcer.enforce();
            fail();
        } catch (AssertionError expected) {
            assertTrue(expected.getMessage().contains(" does not match '.*Ext.*'"));
        }
    }

    @Test
    public void extDeclarationNaming() {
        DDDPlusEnforcer enforcer = new DDDPlusEnforcer();
        enforcer.scanPackages("io.github.enforce.ext_declaration");
        try {
            enforcer.enforce();
            fail();
        } catch (AssertionError expected) {
            assertTrue(expected.getMessage().contains(" does not end with 'Ext'")
                    || expected.getMessage().contains(" does not start with 'I'"));
        }
    }

    @Test
    public void extDeclarationOk() {
        DDDPlusEnforcer enforcer = new DDDPlusEnforcer();
        enforcer.scanPackages("io.github.enforce.ext_ok")
                .enforce();
    }

    @Test
    public void extDeclarationOk1() {
        DDDPlusEnforcer enforcer = new DDDPlusEnforcer();
        enforcer.scanPackages("io.github.enforce.ext_declaration1")
                .enforce();
    }

    @Test
    public void routerNotAnnotatedWithRouter() {
        DDDPlusEnforcer enforcer = new DDDPlusEnforcer();
        enforcer.scanPackages("io.github.enforce.router_annotation");
        try {
            enforcer.enforce();
            fail();
        } catch (AssertionError expected) {
            assertTrue(expected.getMessage().contains(" is not annotated with @Router"));
        }
    }

    @Test
    public void routerNamingHasSuffixRouter() {
        DDDPlusEnforcer enforcer = new DDDPlusEnforcer();
        enforcer.scanPackages("io.github.enforce.router_naming");
        try {
            enforcer.enforce();
            fail();
        } catch (AssertionError expected) {
            assertTrue(expected.getMessage().contains(" does not match '.*ExtRouter'"));
        }
    }

    @Test
    public void patternNaming() {
        DDDPlusEnforcer enforcer = new DDDPlusEnforcer();
        enforcer.scanPackages("io.github.enforce.pattern_naming");
        try {
            enforcer.enforce();
            fail();
        } catch (AssertionError expected) {
            assertTrue(expected.getMessage().contains(" does not match '.*Pattern'"));
        }
    }

    @Test
    public void patternNotIdentityResolver() {
        DDDPlusEnforcer enforcer = new DDDPlusEnforcer();
        enforcer.scanPackages("io.github.enforce.pattern_interface");
        try {
            enforcer.enforce();
            fail();
        } catch (AssertionError expected) {
            assertTrue(expected.getMessage().contains(" is not assignable to io.github.dddplus.ext.IIdentityResolver"));
        }
    }

    @Test
    public void policyWithoutAnnotation() {
        DDDPlusEnforcer enforcer = new DDDPlusEnforcer();
        enforcer.scanPackages("io.github.enforce.policy_annotation");
        try {
            enforcer.enforce();
            fail();
        } catch (AssertionError expected) {
            assertTrue(expected.getMessage().contains(" is not annotated with @Policy"));
        }
    }

    @Test
    public void policyBadNaming() {
        DDDPlusEnforcer enforcer = new DDDPlusEnforcer();
        enforcer.scanPackages("io.github.enforce.policy_naming");
        try {
            enforcer.enforce();
            fail();
        } catch (AssertionError expected) {
            assertTrue(expected.getMessage().contains(" does not match '.*ExtPolicy'"));
        }
    }

}