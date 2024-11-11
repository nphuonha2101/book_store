package com.ecommerce.book_store.persistent.repository.implement;

import com.ecommerce.book_store.persistent.entity.Order;
import org.springframework.stereotype.Repository;

@Repository
public class OrderAdvancedRepositoryImpl extends AdvancedRepositoryImpl<Order> {
    public OrderAdvancedRepositoryImpl() {
        super(Order.class);
    }
}
