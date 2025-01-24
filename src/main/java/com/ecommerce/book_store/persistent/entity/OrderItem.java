package com.ecommerce.book_store.persistent.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "order_items")
@Entity
public class OrderItem extends AbstractEntity{
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "price")
    private double price;

    public OrderItem() {
    }

    public OrderItem(Order order, Book book, int quantity, double price) {
        this.order = order;
        this.book = book;
        this.quantity = quantity;
        this.price = price;
    }

}
