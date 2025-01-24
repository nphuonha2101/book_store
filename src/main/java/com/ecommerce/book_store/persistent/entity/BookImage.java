package com.ecommerce.book_store.persistent.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Table(name="book_images")
@Entity
@Setter
public class BookImage extends AbstractEntity {
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
