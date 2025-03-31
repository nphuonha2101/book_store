package com.ecommerce.book_store.persistent.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@AllArgsConstructor
@Getter
@Setter
public class FcmToken extends AbstractEntity {
    @Column(name = "token")
    private String token;
    @Column(name = "user_id")
    private Long userId;

    public FcmToken() {
    }
}
