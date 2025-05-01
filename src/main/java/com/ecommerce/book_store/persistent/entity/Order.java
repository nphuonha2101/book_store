package com.ecommerce.book_store.persistent.entity;

import com.ecommerce.book_store.core.constant.OrderStatus;
import com.ecommerce.book_store.core.constant.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order extends AbstractEntity implements Serializable {
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "voucher_id")
    private Voucher voucher;
    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;
    @Column(name = "phone")
    private String phone;
    @Column(name = "note")
    private String note;
    @Column(name = "status")
    private OrderStatus status;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<OrderItem> orderItems;
    @Column(name = "total_amount")
    private Double totalAmount;
    @Column(name = "total_discount")
    private Double totalDiscount;
    @Column(name = "shipping_fee")
    private Double shippingFee;
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;
    @Column(name = "cancellation_reason")
    private String cancellationReason;

    public Order(User user, Voucher voucher, Address address, String phone, String note, OrderStatus status, Double totalAmount, Double totalDiscount, Double shippingFee, int paymentMethod, String cancellationReason) {
        this.user = user;
        this.voucher = voucher;
        this.address = address;
        this.phone = phone;
        this.note = note;
        this.status = status;
        this.totalAmount = totalAmount;
        this.totalDiscount = totalDiscount;
        this.shippingFee = shippingFee;
        this.paymentMethod = PaymentMethod.values()[paymentMethod];
        this.cancellationReason = cancellationReason;
    }

    public void setPaymentMethod(int paymentMethod) {
        this.paymentMethod = PaymentMethod.values()[paymentMethod];
    }
}
