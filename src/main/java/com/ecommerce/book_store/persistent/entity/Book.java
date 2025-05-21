package com.ecommerce.book_store.persistent.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name="books")
@Entity
public class Book extends AbstractEntity implements Serializable {
    @Column(name="title")
    private String title;
    @Column(name="author_name")
    private String authorName;
    @Column(name="description", columnDefinition = "TEXT")
    private String description;
    @Column(name="isbn")
    private String isbn;
    @Column(name="cover_image")
    private String coverImage;
    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "book")
    private List<BookImage> images;
    @Column(name="price")
    private int price;
    @Column(name="quantity")
    private int  quantity;
    @Column(name="is_available")
    private boolean isAvailable;
    @Column(name = "published_at")
    private LocalDateTime publishedAt;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="category_id")
    private Category category;
  
    public Book() {
    }

    public Book(String title, String authorName, String description, String isbn, String coverImage, int price, int quantity, boolean isAvailable, LocalDateTime publishedAt, Category category) {
        this.title = title;
        this.authorName = authorName;
        this.description = description;
        this.isbn = isbn;
        this.coverImage = coverImage;
        this.price = price;
        this.quantity = quantity;
        this.isAvailable = isAvailable;
        this.publishedAt = publishedAt;
        this.category = category;
    }


}
