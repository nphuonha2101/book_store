package com.ecommerce.book_store.persistent.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
    @OneToMany(mappedBy = "ribbon")
    @JsonManagedReference
    private List<RibbonItem> ribbonItems;
    @Column(name = "status")
    private boolean status;

}
