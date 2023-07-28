package io.github.dddplus.model;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SetBagTest {

    @Test
    void NPE() {
        try {
            new BookBag(null);
            fail();
        } catch (NullPointerException expected) {
        }
    }

    @Test
    void basic() {
        Set<Book> books = new HashSet<>();
        BookBag bag = new BookBag(books);
        assertTrue(bag.isEmpty());
        assertEquals(0, bag.items().size());
        Book book1 = new Book(1L);
        books.add(book1);
        books.add(new Book((2L)));
        bag = new BookBag(books);
        assertEquals(bag.size(), 2);
        assertFalse(bag.isEmpty());
        assertTrue(bag.anyOne().id > 0);
        assertTrue(bag.contains(book1));
        assertFalse(bag.contains(new Book(1L)));
    }

    @AllArgsConstructor
    static class Book implements IAggregateRoot {
        Long id;
    }

    static class BookException extends RuntimeException {
    }

    static class BookBag extends SetBag<Book> {
        protected BookBag(Set<Book> items) {
            super(items);
        }
    }


}