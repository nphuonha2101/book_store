package com.ecommerce.book_store.persistent.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Table(name = "payments")
@Entity
public class Payment extends AbstractEntity implements Serializable {
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    @Column(name = "payment_method")
    private String paymentMethod;
    @Column(name = "account")
    private String account;
    @Column(name = "transaction_id")
    private String transactionId;
    public Payment() {

    }

    public Payment(Order order, String paymentMethod, String account, String transactionId) {
        this.order = order;
        this.paymentMethod = paymentMethod;
        this.account = account;
        this.transactionId = transactionId;
    }

}
