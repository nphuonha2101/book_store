package com.ecommerce.book_store.persistent.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Table(name = "cart_items")
@Entity
public class CartItem extends AbstractEntity implements Serializable {
    @Column(name = "user_id")
    private Long userId;
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "price")
    private double price;

    public CartItem() {
    }
   public CartItem(Long userId, Book book, int quantity, double price) {
        this.userId = userId;
        this.book = book;
        this.quantity = quantity;
        this.price = price;
    }
}
