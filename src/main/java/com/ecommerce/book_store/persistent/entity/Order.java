package com.ecommerce.book_store.persistent.entity;

import com.ecommerce.book_store.core.constant.OrderStatus;
import com.ecommerce.book_store.persistent.EntityFilterMap;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order extends AbstractEntity {
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "voucher_id")
    private Voucher voucher;
    @Column(name = "address")
    private String address;
    @Column(name = "phone")
    private String phone;
    @Column(name = "note")
    private String note;
    @Column(name = "status")
    private OrderStatus status;
    //    private List<OrderDetail> orderDetails;
    @Column(name = "total_amount")
    private double totalAmount;


    public Order() {
    }

    public Order(User user, Voucher voucher, String address, String phone, String note, OrderStatus status) {
        this.user = user;
        this.voucher = voucher;
        this.address = address;
        this.phone = phone;
        this.note = note;
        this.status = status;

    }

    @Override
    public void initFilterableMap() {
        this.filterMap = new EntityFilterMap();
        this.filterMap.addFilter("user", "status");
    }
}
