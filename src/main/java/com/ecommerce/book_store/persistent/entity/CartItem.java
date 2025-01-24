package com.ecommerce.book_store.persistent.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "cart_items")
@Entity
public class CartItem extends AbstractEntity {
    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "price")
    private double price;

    public CartItem() {
    }

    public CartItem(Cart cart, Book book, int quantity, double price) {
        this.cart = cart;
        this.book = book;
        this.quantity = quantity;
        this.price = price;
    }

}
