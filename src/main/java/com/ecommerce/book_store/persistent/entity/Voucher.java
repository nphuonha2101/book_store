package com.ecommerce.book_store.persistent.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "vouchers")
@Getter
@Setter
public class Voucher extends AbstractEntity implements Serializable {
    @Column(name="thumbnail")
    private String thumbnail;
    @Column(name = "code")
    private String code;
    @Column(name = "discount")
    private int discount;
    @Column(name = "min_spend")
    private int minSpend;
    @Column(name = "expired_date")
    private LocalDateTime expiredDate;
    @ManyToMany
    @JoinTable(
            name = "voucher_categories",
            joinColumns = @JoinColumn(name = "voucher_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories;

    public Voucher() {
    }

    public Voucher(String thumbnail, String code, int discount, int minSpend, LocalDateTime expiredDate) {
        this.thumbnail = thumbnail;
        this.code = code;
        this.discount = discount;
        this.minSpend = minSpend;
        this.expiredDate = expiredDate;
    }

}
