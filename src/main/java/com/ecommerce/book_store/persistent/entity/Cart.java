package com.ecommerce.book_store.persistent.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Table(name = "carts")
@Entity
public class Cart extends AbstractEntity implements Serializable {
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    public Cart() {

    }

    public Cart(User user) {
        this.user = user;
    }

}
