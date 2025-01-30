package com.ecommerce.book_store.persistent.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "admins")
@Getter
@Setter
public class Admin extends AbstractEntity implements Serializable {
    @Column(name = "name")
    private String name;
    @Column(name = "email", unique = true)
    private String email;
    @Column(name = "password")
    private String password;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    public Admin() {
    }

    public Admin(String name, String email, String password, Role role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

}
