package com.ecommerce.book_store.persistent.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "roles")
@Getter
@Setter
public class Role extends AbstractEntity implements Serializable {
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;

    public Role() {
    }

    public Role(String name, String description) {
        this.name = name;
        this.description = description;
    }

}
