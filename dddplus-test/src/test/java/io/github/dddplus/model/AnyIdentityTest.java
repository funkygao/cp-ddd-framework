package io.github.dddplus.model;

import io.github.errcase.pattern.Task;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class AnyIdentityTest {

    @Test
    void basic() {
        // new AnyIdentity() compile err
        Task task = new Task();
        task.setTaskType("T01");
        IBagTest.Order order = new IBagTest.Order();
        order.setOrder("O-A");
        AnyIdentity identity = AnyIdentity.newIdentity();
        identity.put("t", task)
                .put("o", order);
        Task t = identity.get("t", Task.class);
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
    }

}