package com.ecommerce.book_store;

import com.ecommerce.book_store.http.dto.request.implement.BookRequestDto;
import com.ecommerce.book_store.persistent.entity.Book;
import com.ecommerce.book_store.service.abstraction.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
public class BookServiceTest {

    @Autowired
    private BookService bookService;

    @Test
    public void saveBookTest() {
        BookRequestDto bookRequestDto = new BookRequestDto(
                "Test Book",
                "Test Author",
                "Test Description",
                "8748743849384938",
                1000,
                1000,
                true,
                1L,
                LocalDateTime.now(),
                null
        );

        bookService.save(bookRequestDto);
        assert true;
    }

    @Test
    public void deleteBookTest() {
        bookService.deleteById(2L);
        Book book = bookService.findById(2L);
        assert book == null;
    }

    @Test
    public void testGetById() {
        Book book = bookService.findById(3L);
        assert book != null;
    }
}
