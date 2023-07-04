package io.github.dddplus.model;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SetBagTest {

    @Test
    void basic() {
        Set<Book> books = new HashSet<>();
        Book book1 = new Book(1L);
        books.add(book1);
        books.add(new Book((2L)));
        BookBag bag = new BookBag(books);
        assertEquals(bag.size(), 2);
        assertFalse(bag.isEmpty());
        assertTrue(bag.anyItem().id > 0);
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