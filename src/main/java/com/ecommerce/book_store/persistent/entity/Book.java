package com.ecommerce.book_store.persistent.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.ecommerce.book_store.persistent.EntityFilterMap;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name="books")
@Entity
public class Book extends AbstractEntity {
    @Column(name="title")
    private String title;
    @Column(name="author_name")
    private String authorName;
    @Column(name="description")
    private String description;
    @Column(name="isbn")
    private String isbn;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "book")
    private List<BookImage> images;
    @Column(name="price")
    private double price;
    @Column(name="quantity")
    private int  quantity;
    @Column(name="is_available")
    private boolean isAvailable;
    @Column(name = "published_at")
    private LocalDateTime publishedAt;
    @ManyToOne
    @JoinColumn(name="category_id")
    private Category category;
  
    public Book() {
    }

    public Book(String title, String authorName, String description, String isbn, List<BookImage> images, double price, int quantity, boolean isAvailable, LocalDateTime publishedAt, Category category) {
        this.title = title;
        this.authorName = authorName;
        this.description = description;
        this.isbn = isbn;
        this.images = images;
        this.price = price;
        this.quantity = quantity;
        this.isAvailable = isAvailable;
        this.publishedAt = publishedAt;
        this.category = category;
    }


    @Override
    public void initFilterableMap() {
        this.filterMap = new EntityFilterMap();
        this.filterMap.setFilterableKeys(List.of("title", "authorName", "price", "isAvailable", "publishedAt", "category"));

    }

}
