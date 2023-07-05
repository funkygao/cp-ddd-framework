package io.github.dddplus.model;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class ExchangeTest {

    @Test
    public void basic() {
        Exchange exchange = new Exchange();
        assertEquals(0, exchange.size());
        assertFalse(exchange.exists("non-key"));
        assertFalse(exchange.is("non-key"));
        assertNull(exchange.get("non-key", String.class));
        assertNull(exchange.get("non-key", ExchangeTest.class));
        // we can clear on an empty Exchange
        exchange.clear();

        exchange.set("k1", new Date(12345678));
        assertTrue(exchange.exists("k1"));
        assertFalse(exchange.exists("k2"));
        assertEquals(1, exchange.size());
        assertNotNull(exchange.get("k1", Date.class));

        try {
            exchange.get("k1", String.class);
            fail();
        } catch (ClassCastException expected) {

        }

        // overwrite in place
        exchange.set("k1", "foo");
        assertEquals(1, exchange.size());
        assertEquals("foo", exchange.get("k1", String.class));
        try {
            exchange.is("k1");
            fail();
        } catch (ClassCastException expected) {

        }

        // handy boolean method
        exchange.set("k2", true);
        assertTrue(exchange.is("k2"));
        assertFalse(exchange.is("k3"));

        exchange.clear();
        assertEquals(0, exchange.size());
    }

}