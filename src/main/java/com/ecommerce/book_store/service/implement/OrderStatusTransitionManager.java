package com.ecommerce.book_store.service.implement;

import com.ecommerce.book_store.core.constant.OrderStatus;

import java.util.*;

public class OrderStatusTransitionManager {
    private static final Map<OrderStatus, List<OrderStatus>> ALLOWED_TRANSITIONS = new HashMap<>();

    static {
        ALLOWED_TRANSITIONS.put(OrderStatus.PENDING, Arrays.asList(OrderStatus.PROCESSING, OrderStatus.CANCELLED));
        ALLOWED_TRANSITIONS.put(OrderStatus.PROCESSING, Arrays.asList(OrderStatus.SHIPPING, OrderStatus.CANCELLED));
        ALLOWED_TRANSITIONS.put(OrderStatus.SHIPPING, Arrays.asList(OrderStatus.DELIVERED, OrderStatus.CANCELLED));
        ALLOWED_TRANSITIONS.put(OrderStatus.DELIVERED, Collections.emptyList()); // No transitions
        ALLOWED_TRANSITIONS.put(OrderStatus.CANCELLED, Collections.emptyList()); // No transitions
    }

    public static boolean isValidTransition(OrderStatus currentStatus, OrderStatus newStatus) {
        return ALLOWED_TRANSITIONS.getOrDefault(currentStatus, Collections.emptyList()).contains(newStatus);
    }

    public static List<OrderStatus> getAllowedTransitions(OrderStatus currentStatus) {
        return ALLOWED_TRANSITIONS.getOrDefault(currentStatus, Collections.emptyList());
    }
}
