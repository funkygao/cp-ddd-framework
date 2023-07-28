package io.github.dddplus.model;

import io.github.dddplus.ext.AnyIdentity;
import io.github.errcase.pattern.FooTask;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class AnyIdentityTest {

    @Test
    void basic() {
        // new AnyIdentity() compile err
        FooTask fooTask = new FooTask();
        fooTask.setTaskType("T01");
        IBagTest.Order order = new IBagTest.Order();
        order.setOrder("O-A");
        AnyIdentity identity = AnyIdentity.newIdentity();
        identity.put("t", fooTask)
                .put("o", order);
        FooTask t = identity.get("t", FooTask.class);
        assertEquals("T01", t.getTaskType());
        IBagTest.Order o = identity.get("o", IBagTest.Order.class);
        assertEquals("O-A", o.getOrder());

        // edge cases
        String s = identity.get("non-exist", String.class);
        assertNull(s);

        try {
            Date date = identity.get("o", Date.class);
            fail();
        } catch (ClassCastException expected) {
        }

        try {
            identity.put(null, null);
            fail();
        } catch (NullPointerException expected) {
        }

        try {
            identity.get(null, null);
            fail();
        } catch (NullPointerException expected) {
        }
        try {
            identity.get(null, Date.class);
            fail();
        } catch (NullPointerException expected) {
        }
        try {
            identity.get("xxx", null);
            fail();
        } catch (NullPointerException expected) {
        }
    }

}