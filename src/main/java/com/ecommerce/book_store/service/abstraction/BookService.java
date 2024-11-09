package com.ecommerce.book_store.service.abstraction;

import com.ecommerce.book_store.persistent.entity.Book;
import org.springframework.stereotype.Service;

@Service
public interface BookService extends IService<Book> {
}
