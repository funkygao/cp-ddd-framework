package io.github.dddplus.model.specification;

import io.github.dddplus.model.spcification.Notification;
import org.junit.Test;

import static org.junit.Assert.*;

public class NotificationTest {

    @Test
    public void basic() {
        Notification notification = Notification.build();
        assertFalse(notification.hasError());
        notification.addError("");
        notification.addError(null);
        notification.addError("  ");
        assertFalse(notification.hasError());
        assertNull(notification.first());

        notification.addError("xx");
        assertTrue(notification.hasError());
        assertEquals("xx", notification.first());
        assertEquals(1, notification.getErrors().size());

        notification.addError("yy");
        assertEquals(2, notification.getErrors().size());
        assertEquals("xx", notification.first());

        assertNotNull(notification.firstArgs());
        assertEquals(0, notification.firstArgs().length);
    }

    @Test
    public void i18n() {
        Notification notification = Notification.build();
        notification.addError("111", "a", 18);
        Object[] args = notification.firstArgs();
        assertEquals("a", args[0]);
        assertEquals(18, args[1]);
        assertEquals(2, args.length);
        assertEquals("111", notification.first());
    }

}
