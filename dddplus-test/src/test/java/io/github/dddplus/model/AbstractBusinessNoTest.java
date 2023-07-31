package io.github.dddplus.model;

import lombok.NonNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

import nl.jqno.equalsverifier.EqualsVerifier;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class AbstractBusinessNoTest {

    static class Foo extends AbstractBusinessNo<Integer> {
        protected Foo(Integer value) {
            super(value);
        }
    }

    static class Bar extends AbstractBusinessNo<String> {
        protected Bar(@NonNull String value) {
            super(value);
        }
    }

    @Test
    void present() {
        Foo foo = new Foo(null);
        assertFalse(foo.isPresent());
        foo = new Foo(19);
        assertTrue(foo.isPresent());
        assertFalse(foo.toString().isEmpty());
    }

    @Test
    void basic() {
        Integer i = 87;
        Foo foo = new Foo(i);
        assertEquals(foo, i);
        assertTrue(foo.equals(i));
        assertFalse(foo.equals(109));
        assertFalse(foo.equals(new Date()));
        assertFalse("87".equals(foo));
        assertEquals(new Foo(i), foo);
        assertEquals(i, foo.value());

        // i is Integer, whose equals() does not recognize Foo
        assertFalse(i.equals(foo));

        Bar bar = new Bar("87");
        assertNotEquals(bar, foo);
        assertFalse(foo.equals(bar));
        assertFalse(bar.equals(foo));
    }

    @Test
    @Disabled
    void equalsContract() {
        //EqualsVerifier.forClass(Foo.class).verify();
    }

}