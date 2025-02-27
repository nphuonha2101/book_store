package com.ecommerce.book_store.persistent.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
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
    @Column(name = "description")
    private String description;

    @ManyToMany(mappedBy = "categories")
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
