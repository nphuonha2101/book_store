package com.ecommerce.book_store.service.implement;

import com.ecommerce.book_store.persistent.entity.Book;
import com.ecommerce.book_store.persistent.repository.abstraction.BookRepository;
import com.ecommerce.book_store.persistent.repository.implement.BookAdvancedRepositoryImpl;
import com.ecommerce.book_store.service.abstraction.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BookServiceImpl implements BookService {
    private final BookAdvancedRepositoryImpl bookAdvancedRepository;
    private final BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(BookAdvancedRepositoryImpl bookAdvancedRepository, BookRepository bookRepository) {
        this.bookAdvancedRepository = bookAdvancedRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public Book findById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public List<Book> findAll(Sort sort) {
        return bookRepository.findAll(sort);
    }

    @Override
    public List<Book> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable).getContent();
    }

    @Override
    public List<Book> findAllByCriteria(Map<String, Object> criteria, Pageable pageable) {
        return bookAdvancedRepository.findAllByCriteria(criteria, pageable);
    }

    @Override
    public List<Book> findAllDeleted(Pageable pageable) {
        return bookAdvancedRepository.findAllDeleted(pageable);
    }

    @Override
    public List<Book> findAllDeletedByCriteria(Map<String, Object> criteria, Pageable pageable) {
        return bookAdvancedRepository.findAllDeletedByCriteria(criteria, pageable);
    }

    @Override
    public Book save(Book T) {
        return bookRepository.save(T);
    }

    @Override
    public Book update(Book T) {
        return bookRepository.save(T);
    }

    @Override
    public boolean deleteById(Long id) {
        bookRepository.deleteById(id);
        return true;
    }

    @Override
    public int deleteByIds(List<Long> ids) {
        return bookAdvancedRepository.deleteByIds(ids);
    }

    @Override
    public boolean softDeleteById(Long id) {
        return bookAdvancedRepository.softDeleteById(id);
    }

    @Override
    public int softDeleteByIds(List<Long> ids) {
        return bookAdvancedRepository.softDeleteByIds(ids);
    }

    @Override
    public int restoreByIds(List<Long> ids) {
        return bookAdvancedRepository.restoreByIds(ids);
    }
}
