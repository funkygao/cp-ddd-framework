package io.github.dddplus.specification;

import org.junit.Test;

import static org.junit.Assert.*;

public class NotificationTest {

    @Test
    public void basic() {
        Notification notification = Notification.create();
        assertTrue(notification.isEmpty());
        notification.addReason("a");
        assertEquals(1, notification.size());
        assertFalse(notification.isEmpty());
        assertEquals("a", notification.firstReason());
        assertEquals("a", notification.reasons().get(0));
        notification.addReason("大");
        assertEquals(2, notification.size());
        assertEquals("a", notification.firstReason());
        assertEquals("大", notification.reasons().get(1));
    }

    @Test
    public void addEmptyReason() {
        Notification notification = Notification.create();
        assertTrue(notification.isEmpty());
        assertEquals(0, notification.size());
        assertFalse(notification.addReason(null));
        assertTrue(notification.isEmpty());
        assertFalse(notification.addReason(""));
        assertTrue(notification.isEmpty());
        assertFalse(notification.addReason(" ")); // trim
        assertTrue(notification.isEmpty());
        assertNull(notification.firstReason());

        assertTrue(notification.addReason("a"));
        assertFalse(notification.isEmpty());
        assertEquals(1, notification.reasons().size());
        assertEquals(1, notification.size());
        assertEquals("a", notification.firstReason());

    }

    @Test
    public void dupCheck() {
        Notification notification = Notification.create();
        assertTrue(notification.addReason("中国"));
        assertEquals(1, notification.reasons().size());
        assertFalse(notification.addReason("中国"));
        assertEquals(1, notification.reasons().size());
    }

}