package com.ecommerce.book_store.persistent.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Table(name="book_images")
@Entity
@Setter
public class BookImage extends AbstractEntity implements Serializable {
    @Column(name="url")
    private String url;
    @ManyToOne
    @JoinColumn(name="book_id", insertable = false, updatable = false)
    protected Book book;
    @Column(name="book_id")
    private Long bookId;

    public BookImage() {
    }

    public BookImage(String url, Book book) {
        this.url = url;
        this.book = book;
    }

}
