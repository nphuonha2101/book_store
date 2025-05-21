package com.ecommerce.book_store.persistent.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
@Entity
@Table(name = "ribbon_items")
@NoArgsConstructor
public class RibbonItem extends AbstractEntity implements Serializable {
    @ManyToOne
    @JoinColumn(name= "book_id", insertable = false, updatable = false)
    private Book book;
    @Column(name = "book_id")
    private Long bookId;
    @Column(name = "ribbon_id")
    private Long ribbonId;
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "ribbon_id", insertable = false, updatable = false)
    private Ribbon ribbon;

}
