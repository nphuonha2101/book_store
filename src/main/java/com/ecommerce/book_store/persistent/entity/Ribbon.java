package com.ecommerce.book_store.persistent.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "ribbons")
public class Ribbon extends AbstractEntity implements Serializable {
    @Column(name = "name")
    private String name;
    @Column(name = "description", nullable = true)
    private String description;
    @OneToMany(mappedBy = "ribbon", cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<RibbonItem> ribbonItems;
    @Column(name = "status")
    private boolean status;

}
