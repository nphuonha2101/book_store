package com.ecommerce.book_store.persistent.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
@Getter
@Setter
@Entity
@Table(name = "wish_lists")
public class WishList extends AbstractEntity implements Serializable {

    @Column(name = "user_id")
    private Long userId;
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;
    public WishList() {
    }
    public WishList(Long userId, Book book) {
        this.userId = userId;
        this.book = book;
    }
}
