package com.ecommerce.book_store.persistent.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.awt.print.Book;

@Getter
@Setter
@Table(name="book_images")
public class BookImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", insertable = false, updatable = false)
    private Long id;
    @Column(name="url")
    private String url;
    @ManyToOne
    @JoinColumn(name="book_id")
    protected Book book;

    public BookImage() {
    }

    public BookImage(String url, Book book) {
        this.url = url;
        this.book = book;
    }
}
