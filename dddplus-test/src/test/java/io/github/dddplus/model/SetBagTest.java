package io.github.dddplus.model;

import io.github.dddplus.model.spcification.AbstractSpecification;
import io.github.dddplus.model.spcification.Notification;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SetBagTest {

    @Test
    void basic() {
        Set<Book> books = new HashSet<>();
        books.add(new Book(1L));
        books.add(new Book((2L)));
        BookBag bag = new BookBag(books);
        assertEquals(bag.size(), 2);
        assertFalse(bag.isEmpty());
        try {
            bag.satisfy(new BookSpec());
            fail();
        } catch (RuntimeException expected) {
            // FIXME
            //assertTrue(expected instanceof BookException);
        }
        assertTrue(bag.anyItem().id > 0);
    }

    @AllArgsConstructor
    static class Book implements IAggregateRoot {
        Long id;
    }

    static class BookException extends RuntimeException {
    }

    static class BookSpec extends AbstractSpecification<Book> {
        @Override
        public boolean isSatisfiedBy(Book candidate, Notification notification) {
            return false;
        }
    }

    static class BookBag extends SetBag<Book, BookException> {
        protected BookBag(Set<Book> items) {
            super(items);
        }
    }


}