package io.github.dddplus.model;

import io.github.dddplus.model.spcification.AbstractSpecification;
import io.github.dddplus.model.spcification.Notification;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ListBagTest {

    @Test
    void basic() {
        List<Book> books = new ArrayList<>();
        books.add(new Book(1L));
        books.add(new Book((2L)));
        BookBag bag = new BookBag(books);
        assertEquals(bag.size(), 2);
        assertFalse(bag.isEmpty());
        try {
            bag.satisfy(new BookSpec());
            fail();
        } catch (RuntimeException expected) {
            assertTrue(expected instanceof BookException);
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

    static class BookBag extends ListBag<Book> {
        protected BookBag(List<Book> items) {
            super(items);
        }

        @Override
        protected void whenNotSatisfied(Notification notification) {
            throw new BookException();
        }
    }


}