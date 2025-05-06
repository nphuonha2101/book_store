package com.ecommerce.book_store.persistent.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@AllArgsConstructor
@Getter
@Setter
@Table(name = "fcm_tokens")
public class FcmToken extends AbstractEntity {
    @Column(name = "token")
    private String token;
    @Column(name = "user_id")
    private Long userId;

    public FcmToken() {
    }
}
