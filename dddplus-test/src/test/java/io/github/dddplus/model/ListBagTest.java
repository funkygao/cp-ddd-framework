package io.github.dddplus.model;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ListBagTest {

    @Test
    void basic() {
        try {
            new BookBag(null);
            fail();
        } catch (NullPointerException expected) {

        }

        List<Book> books = new ArrayList<>();
        BookBag bag = new BookBag(books);
        assertTrue(bag.isEmpty());
        assertEquals(bag.items().size(), 0);

        Book book1 = new Book(1L);
        books.add(book1);
        books.add(new Book((2L)));
        bag = new BookBag(books);
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

    static class BookBag extends ListBag<Book> {
        protected BookBag(List<Book> items) {
            super(items);
        }
    }


}