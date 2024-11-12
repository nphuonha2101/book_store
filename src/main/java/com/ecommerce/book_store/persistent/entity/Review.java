package com.ecommerce.book_store.persistent.entity;

import com.ecommerce.book_store.persistent.EntityFilterMap;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Table(name = "reviews")
@Entity
public class Review extends AbstractEntity {
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;
    @Column(name = "rating")
    private int rating;
    @Column(name = "comment")
    private String coomment;

    public Review() {
    }

    public Review(User user, Book book, int rating, String coomment) {
        this.user = user;
        this.book = book;
        this.rating = rating;
        this.coomment = coomment;
    }

    @Override
    public void initFilterableMap() {
        this.filterMap = new EntityFilterMap();
        this.filterMap.setFilterableKeys(List.of("user", "book", "rating", "comment"));
    }
}
