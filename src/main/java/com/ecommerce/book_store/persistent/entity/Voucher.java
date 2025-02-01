package com.ecommerce.book_store.persistent.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "vouchers")
@Getter
@Setter
public class Voucher extends AbstractEntity implements Serializable {
    @Column(name = "code")
    private String code;
    @Column(name = "discount")
    private double discount;
    @Column(name = "min_spend")
    private double min_spend;
    @Column(name = "expired_date")
    private double expired_date;
    @ManyToMany
    @JoinTable(
            name = "voucher_categories",
            joinColumns = @JoinColumn(name = "voucher_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories;

    public Voucher() {
    }

    public Voucher(String code, double discount, double min_spend, double expired_date) {
        this.code = code;
        this.discount = discount;
        this.min_spend = min_spend;
        this.expired_date = expired_date;
    }

}
