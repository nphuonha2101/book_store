package com.ecommerce.book_store.persistent.repository.implement;

import com.ecommerce.book_store.persistent.entity.Book;
import org.springframework.stereotype.Repository;

@Repository
public class BookAdvancedRepositoryImpl extends AdvancedRepositoryImpl<Book> {
    public BookAdvancedRepositoryImpl() {
        super(Book.class);
    }
}
