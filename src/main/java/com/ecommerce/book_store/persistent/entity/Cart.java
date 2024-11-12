package com.ecommerce.book_store.persistent.entity;

import com.ecommerce.book_store.persistent.EntityFilterMap;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Table(name = "carts")
@Entity
public class Cart extends AbstractEntity{
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    public Cart() {

    }

    public Cart(User user) {
        this.user = user;
    }

    @Override
    public void initFilterableMap() {
        this.filterMap = new EntityFilterMap();
        this.filterMap.setFilterableKeys(List.of("user"));
    }
}
