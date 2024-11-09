package com.ecommerce.book_store.persistent.repository.implement;

import com.ecommerce.book_store.persistent.entity.Book;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;


@org.springframework.stereotype.Repository
public class BookRepository extends Repository<Book> {
    @PersistenceContext
    private EntityManager entityManager;


    public BookRepository(Class<Book> entityClass) {
        super(entityClass);
    }


}
