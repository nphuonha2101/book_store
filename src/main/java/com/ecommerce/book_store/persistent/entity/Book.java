package com.ecommerce.book_store.persistent.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.ecommerce.book_store.persistent.EntityFilterMap;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name="books")
public class  Book extends AbstractEntity {
    @Column(name="title")
    private String title;
    @Column(name="author")
    private String author;
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
  
    public Book() {
    }

    public Book(String title, String author, String description, String isbn, List<BookImage> images, double price, int quantity, boolean isAvailable) {
        this.title = title;
        this.author = author;
            
        this.description = description;
        this.isbn = isbn;
        this.images = images;
        this.price = price;
        this.quantity = quantity;
        this.isAvailable = isAvailable;
    }


    @Override
    public void initFilterMap() {
        this.filterMap = new EntityFilterMap();
        filterMap.addFilter("title", null);
        filterMap.addFilter("author", null);
        filterMap.addFilter("description", null);
        filterMap.addFilter("isbn", null);
        filterMap.addFilter("price", null);
        filterMap.addFilter("quantity", null);
        filterMap.addFilter("isAvailable", null);

    }

    @Override
    public void addFilter(String key, Object value) {
        this.filterMap.addFilter(key, value);
    }
}
