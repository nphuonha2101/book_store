package com.ecommerce.book_store.persistent.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "categories")
@Getter
@Setter
public class Category extends AbstractEntity implements Serializable {
    @Column(name = "name")
    private String name;
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @ManyToMany(mappedBy = "categories", cascade = CascadeType.PERSIST)
    @JsonIgnore
    private List<Voucher> vouchers;

    public Category() {
    }

    public Category(Long id) {
        super(id);
    }

    public Category(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
